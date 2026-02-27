package com.avinashpatil.app.youtube.ui.sheets

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.api.obj.StreamItem
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.enums.ShareObjectType
import com.avinashpatil.app.youtube.extensions.parcelable
import com.avinashpatil.app.youtube.extensions.serializable
import com.avinashpatil.app.youtube.extensions.toID
import com.avinashpatil.app.youtube.helpers.BackgroundHelper
import com.avinashpatil.app.youtube.helpers.ContextHelper
import com.avinashpatil.app.youtube.helpers.NavigationHelper
import com.avinashpatil.app.youtube.obj.ShareData
import com.avinashpatil.app.youtube.ui.activities.NoInternetActivity
import com.avinashpatil.app.youtube.ui.dialogs.ShareDialog
import com.avinashpatil.app.youtube.ui.fragments.DownloadTab
import com.avinashpatil.app.youtube.util.PlayingQueue
import com.avinashpatil.app.youtube.util.PlayingQueueMode

class DownloadOptionsBottomSheet : BaseBottomSheet() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val streamItem = arguments?.parcelable<StreamItem>(IntentData.streamItem)!!
        val videoId = streamItem.url!!.toID()
        val downloadTab = arguments?.serializable<DownloadTab>(IntentData.downloadTab)!!
        val playlistId = arguments?.getString(IntentData.playlistId)

        val options = mutableListOf(
            R.string.playOnBackground,
            R.string.share,
            R.string.delete
        )

        // can't navigate to video while in offline activity
        if (ContextHelper.tryUnwrapActivity<NoInternetActivity>(requireContext()) == null) {
            options += R.string.go_to_video
        }

        val isSelectedVideoCurrentlyPlaying = PlayingQueue.getCurrent()?.url?.toID() == videoId
        if (!isSelectedVideoCurrentlyPlaying && PlayingQueue.isNotEmpty() && PlayingQueue.queueMode == PlayingQueueMode.OFFLINE) {
            options += R.string.play_next
            options += R.string.add_to_queue
        }

        setSimpleItems(options.map { getString(it) }) { which ->
            when (options[which]) {
                R.string.playOnBackground -> {
                    BackgroundHelper.playOnBackgroundOffline(requireContext(), videoId, playlistId, downloadTab)
                }

                R.string.go_to_video -> {
                    NavigationHelper.navigateVideo(requireContext(), videoId = videoId)
                }

                R.string.share -> {
                    val shareData = ShareData(currentVideo = videoId)
                    val bundle = bundleOf(
                        IntentData.id to videoId,
                        IntentData.shareObjectType to ShareObjectType.VIDEO,
                        IntentData.shareData to shareData
                    )
                    val newShareDialog = ShareDialog()
                    newShareDialog.arguments = bundle
                    newShareDialog.show(parentFragmentManager, null)
                }

                R.string.delete -> {
                    setFragmentResult(DELETE_DOWNLOAD_REQUEST_KEY, bundleOf())
                    dialog?.dismiss()
                }

                R.string.play_next -> {
                    PlayingQueue.addAsNext(streamItem)
                }

                R.string.add_to_queue -> {
                    PlayingQueue.add(streamItem)
                }
            }
        }

        super.onCreate(savedInstanceState)
    }

    companion object {
        const val DELETE_DOWNLOAD_REQUEST_KEY = "delete_download_request_key"
    }
}
