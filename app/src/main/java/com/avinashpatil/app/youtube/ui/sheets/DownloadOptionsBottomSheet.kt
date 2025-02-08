package com.avinashpatil.app.youtube.ui.sheets

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.enums.ShareObjectType
import com.avinashpatil.app.youtube.extensions.serializable
import com.avinashpatil.app.youtube.helpers.BackgroundHelper
import com.avinashpatil.app.youtube.helpers.ContextHelper
import com.avinashpatil.app.youtube.helpers.NavigationHelper
import com.avinashpatil.app.youtube.obj.ShareData
import com.avinashpatil.app.youtube.ui.activities.DownloadActivity
import com.avinashpatil.app.youtube.ui.activities.NoInternetActivity
import com.avinashpatil.app.youtube.ui.dialogs.ShareDialog
import com.avinashpatil.app.youtube.ui.fragments.DownloadTab

class DownloadOptionsBottomSheet : BaseBottomSheet() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val videoId = arguments?.getString(IntentData.videoId)!!
        val downloadTab = arguments?.serializable<DownloadTab>(IntentData.downloadTab)!!

        val options = mutableListOf(
            R.string.playOnBackground,
            R.string.go_to_video,
            R.string.share,
            R.string.delete
        )

        // can't navigate to video while in offline activity
        if (ContextHelper.tryUnwrapActivity<NoInternetActivity>(requireContext()) != null) {
            options.remove(R.string.go_to_video)
        }

        setSimpleItems(options.map { getString(it) }) { which ->
            when (options[which]) {
                R.string.playOnBackground -> {
                    BackgroundHelper.playOnBackgroundOffline(requireContext(), videoId, downloadTab)
                }

                R.string.go_to_video -> {
                    NavigationHelper.navigateVideo(requireContext(), videoUrlOrId = videoId)
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
            }
        }

        super.onCreate(savedInstanceState)
    }

    companion object {
        const val DELETE_DOWNLOAD_REQUEST_KEY = "delete_download_request_key"
    }
}
