package com.avinashpatil.app.youtube.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.databinding.PlaylistsRowBinding
import com.avinashpatil.app.youtube.db.DatabaseHolder
import com.avinashpatil.app.youtube.db.obj.PlaylistBookmark
import com.avinashpatil.app.youtube.enums.PlaylistType
import com.avinashpatil.app.youtube.helpers.ImageHelper
import com.avinashpatil.app.youtube.helpers.NavigationHelper
import com.avinashpatil.app.youtube.ui.adapters.callbacks.DiffUtilItemCallback
import com.avinashpatil.app.youtube.ui.base.BaseActivity
import com.avinashpatil.app.youtube.ui.sheets.PlaylistOptionsBottomSheet
import com.avinashpatil.app.youtube.ui.viewholders.PlaylistBookmarkViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistBookmarkAdapter: ListAdapter<PlaylistBookmark, PlaylistBookmarkViewHolder>(
    DiffUtilItemCallback(
        areItemsTheSame = { oldItem, newItem -> oldItem.playlistId == newItem.playlistId }
    )
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistBookmarkViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistBookmarkViewHolder(
            PlaylistsRowBinding.inflate(layoutInflater, parent, false)
        )
    }

    private fun showPlaylistOptions(context: Context, bookmark: PlaylistBookmark) {
        val sheet = PlaylistOptionsBottomSheet()
        sheet.arguments = bundleOf(
            IntentData.playlistId to bookmark.playlistId,
            IntentData.playlistName to bookmark.playlistName,
            IntentData.playlistType to PlaylistType.PUBLIC
        )
        sheet.show(
            (context as BaseActivity).supportFragmentManager
        )
    }

    override fun onBindViewHolder(holder: PlaylistBookmarkViewHolder, position: Int) {
        val bookmark = getItem(holder.bindingAdapterPosition)

        with(holder.binding) {
            var isBookmarked = true

            ImageHelper.loadImage(bookmark.thumbnailUrl, playlistThumbnail)
            playlistTitle.text = bookmark.playlistName
            playlistDescription.text = bookmark.uploader
            videoCount.text = bookmark.videos.toString()

            bookmarkPlaylist.setOnClickListener {
                isBookmarked = !isBookmarked
                bookmarkPlaylist.setImageResource(
                    if (isBookmarked) R.drawable.ic_bookmark else R.drawable.ic_bookmark_outlined
                )
                CoroutineScope(Dispatchers.IO).launch {
                    if (!isBookmarked) {
                        DatabaseHolder.Database.playlistBookmarkDao()
                            .deleteById(bookmark.playlistId)
                    } else {
                        DatabaseHolder.Database.playlistBookmarkDao().insert(bookmark)
                    }
                }
            }
            bookmarkPlaylist.isVisible = true

            root.setOnClickListener {
                NavigationHelper.navigatePlaylist(
                    root.context,
                    bookmark.playlistId,
                    PlaylistType.PUBLIC
                )
            }

            root.setOnLongClickListener {
                showPlaylistOptions(root.context, bookmark)
                true
            }
        }
    }
}