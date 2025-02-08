package com.avinashpatil.app.youtube.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.api.JsonHelper
import com.avinashpatil.app.youtube.api.obj.ContentItem
import com.avinashpatil.app.youtube.api.obj.StreamItem
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.databinding.ChannelRowBinding
import com.avinashpatil.app.youtube.databinding.PlaylistsRowBinding
import com.avinashpatil.app.youtube.databinding.VideoRowBinding
import com.avinashpatil.app.youtube.db.DatabaseHolder
import com.avinashpatil.app.youtube.enums.PlaylistType
import com.avinashpatil.app.youtube.extensions.formatShort
import com.avinashpatil.app.youtube.extensions.toID
import com.avinashpatil.app.youtube.helpers.ImageHelper
import com.avinashpatil.app.youtube.helpers.NavigationHelper
import com.avinashpatil.app.youtube.ui.adapters.callbacks.DiffUtilItemCallback
import com.avinashpatil.app.youtube.ui.base.BaseActivity
import com.avinashpatil.app.youtube.ui.extensions.setFormattedDuration
import com.avinashpatil.app.youtube.ui.extensions.setWatchProgressLength
import com.avinashpatil.app.youtube.ui.extensions.setupSubscriptionButton
import com.avinashpatil.app.youtube.ui.sheets.ChannelOptionsBottomSheet
import com.avinashpatil.app.youtube.ui.sheets.PlaylistOptionsBottomSheet
import com.avinashpatil.app.youtube.ui.sheets.VideoOptionsBottomSheet
import com.avinashpatil.app.youtube.ui.viewholders.SearchViewHolder
import com.avinashpatil.app.youtube.util.TextUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString

class SearchResultsAdapter(
    private val timeStamp: Long = 0
) : PagingDataAdapter<ContentItem, SearchViewHolder>(
    DiffUtilItemCallback(
        areItemsTheSame = { oldItem, newItem -> oldItem.url == newItem.url },
        areContentsTheSame = { _, _ -> true },
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            0 -> SearchViewHolder(
                VideoRowBinding.inflate(layoutInflater, parent, false)
            )

            1 -> SearchViewHolder(
                ChannelRowBinding.inflate(layoutInflater, parent, false)
            )

            2 -> SearchViewHolder(
                PlaylistsRowBinding.inflate(layoutInflater, parent, false)
            )

            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchItem = getItem(position)!!

        val videoRowBinding = holder.videoRowBinding
        val channelRowBinding = holder.channelRowBinding
        val playlistRowBinding = holder.playlistRowBinding

        if (videoRowBinding != null) {
            bindVideo(searchItem, videoRowBinding, position)
        } else if (channelRowBinding != null) {
            bindChannel(searchItem, channelRowBinding)
        } else if (playlistRowBinding != null) {
            bindPlaylist(searchItem, playlistRowBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.type) {
            StreamItem.TYPE_STREAM -> 0
            StreamItem.TYPE_CHANNEL -> 1
            StreamItem.TYPE_PLAYLIST -> 2
            else -> 3
        }
    }

    private fun bindVideo(item: ContentItem, binding: VideoRowBinding, position: Int) {
        binding.apply {
            ImageHelper.loadImage(item.thumbnail, thumbnail)
            thumbnailDuration.setFormattedDuration(item.duration, item.isShort, item.uploaded)
            videoTitle.text = item.title
            videoInfo.text = TextUtils.formatViewsString(root.context, item.views, item.uploaded)

            channelName.text = item.uploaderName
            ImageHelper.loadImage(item.uploaderAvatar, channelImage, true)

            root.setOnClickListener {
                NavigationHelper.navigateVideo(root.context, item.url, timestamp = timeStamp)
            }

            val videoId = item.url.toID()
            val activity = (root.context as BaseActivity)
            val fragmentManager = activity.supportFragmentManager
            root.setOnLongClickListener {
                fragmentManager.setFragmentResultListener(
                    VideoOptionsBottomSheet.VIDEO_OPTIONS_SHEET_REQUEST_KEY,
                    activity
                ) { _, _ ->
                    notifyItemChanged(position)
                }
                val sheet = VideoOptionsBottomSheet()
                val contentItemString = JsonHelper.json.encodeToString(item)
                val streamItem: StreamItem = JsonHelper.json.decodeFromString(contentItemString)
                sheet.arguments = bundleOf(IntentData.streamItem to streamItem)
                sheet.show(fragmentManager, SearchResultsAdapter::class.java.name)
                true
            }
            channelContainer.setOnClickListener {
                NavigationHelper.navigateChannel(root.context, item.uploaderUrl)
            }
            watchProgress.setWatchProgressLength(videoId, item.duration)

            CoroutineScope(Dispatchers.IO).launch {
                val isDownloaded =
                    DatabaseHolder.Database.downloadDao().exists(videoId)

                withContext(Dispatchers.Main) {
                    downloadBadge.isVisible = isDownloaded
                }
            }
        }
    }

    private fun bindChannel(item: ContentItem, binding: ChannelRowBinding) {
        binding.apply {
            ImageHelper.loadImage(item.thumbnail, searchChannelImage, true)
            searchChannelName.text = item.name

            val subscribers = item.subscribers.formatShort()
            searchViews.text = if (item.subscribers >= 0 && item.videos >= 0) {
                root.context.getString(R.string.subscriberAndVideoCounts, subscribers, item.videos)
            } else if (item.subscribers >= 0) {
                root.context.getString(R.string.subscribers, subscribers)
            } else if (item.videos >= 0) {
                root.context.getString(R.string.videoCount, item.videos)
            } else {
                ""
            }

            root.setOnClickListener {
                NavigationHelper.navigateChannel(root.context, item.url)
            }

            var subscribed = false
            binding.searchSubButton.setupSubscriptionButton(item.url.toID(), item.name?.toID()) {
                subscribed = it
            }

            root.setOnLongClickListener {
                val channelOptionsSheet = ChannelOptionsBottomSheet()
                channelOptionsSheet.arguments = bundleOf(
                    IntentData.channelId to item.url.toID(),
                    IntentData.channelName to item.name,
                    IntentData.isSubscribed to subscribed
                )
                channelOptionsSheet.show((root.context as BaseActivity).supportFragmentManager)
                true
            }
        }
    }

    private fun bindPlaylist(item: ContentItem, binding: PlaylistsRowBinding) {
        binding.apply {
            ImageHelper.loadImage(item.thumbnail, playlistThumbnail)
            if (item.videos >= 0) videoCount.text = item.videos.toString()
            playlistTitle.text = item.name
            playlistDescription.text = item.uploaderName
            root.setOnClickListener {
                NavigationHelper.navigatePlaylist(root.context, item.url, PlaylistType.PUBLIC)
            }

            root.setOnLongClickListener {
                val playlistId = item.url.toID()
                val playlistName = item.name!!
                val sheet = PlaylistOptionsBottomSheet()
                sheet.arguments = bundleOf(
                    IntentData.playlistId to playlistId,
                    IntentData.playlistName to playlistName,
                    IntentData.playlistType to PlaylistType.PUBLIC
                )
                sheet.show(
                    (root.context as BaseActivity).supportFragmentManager,
                    PlaylistOptionsBottomSheet::class.java.name
                )
                true
            }
        }
    }
}
