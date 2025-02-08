package com.avinashpatil.app.youtube.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.api.PlaylistsHelper
import com.avinashpatil.app.youtube.api.obj.Playlists
import com.avinashpatil.app.youtube.api.obj.StreamItem
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.databinding.DialogAddToPlaylistBinding
import com.avinashpatil.app.youtube.extensions.TAG
import com.avinashpatil.app.youtube.extensions.parcelable
import com.avinashpatil.app.youtube.extensions.toastFromMainDispatcher
import com.avinashpatil.app.youtube.ui.models.PlaylistViewModel
import com.avinashpatil.app.youtube.util.PlayingQueue
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

/**
 * Dialog to insert new videos to a playlist
 * videoId: The id of the video to add. If non is provided, insert the whole playing queue
 */
class AddToPlaylistDialog : DialogFragment() {
    private var videoInfo: StreamItem? = null
    private val viewModel: PlaylistViewModel by activityViewModels()

    var playlists = emptyList<Playlists>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoInfo = arguments?.parcelable(IntentData.videoInfo)
        Log.e("video info", videoInfo.toString())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddToPlaylistBinding.inflate(layoutInflater)

        childFragmentManager.setFragmentResultListener(
            CreatePlaylistDialog.CREATE_PLAYLIST_DIALOG_REQUEST_KEY,
            this
        ) { _, resultBundle ->
            val addedToPlaylist = resultBundle.getBoolean(IntentData.playlistTask)
            if (addedToPlaylist) {
                fetchPlaylists(binding)
            }
        }

        fetchPlaylists(binding)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.addToPlaylist)
            .setNegativeButton(R.string.createPlaylist, null)
            .setPositiveButton(R.string.addToPlaylist, null)
            .setView(binding.root)
            .show()
            .apply {
                getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    CreatePlaylistDialog().show(childFragmentManager, null)
                }
                getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    val playlistIndex = binding.playlistsSpinner.selectedItemPosition

                    val playlist = playlists.getOrElse(playlistIndex) { return@setOnClickListener }
                    viewModel.lastSelectedPlaylistId = playlist.id!!

                    dialog?.hide()
                    lifecycleScope.launch {
                        addToPlaylist(playlist.id, playlist.name!!)
                        dialog?.dismiss()
                    }
                }
            }
    }

    private fun fetchPlaylists(binding: DialogAddToPlaylistBinding) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                playlists = try {
                    PlaylistsHelper.getPlaylists()
                } catch (e: Exception) {
                    Log.e(TAG(), e.toString())
                    Toast.makeText(context, R.string.unknown_error, Toast.LENGTH_SHORT).show()
                    return@repeatOnLifecycle
                }.filter { !it.name.isNullOrEmpty() }

                binding.playlistsSpinner.items = playlists.map { it.name!! }

                if (playlists.isEmpty()) return@repeatOnLifecycle

                // select the last used playlist
                viewModel.lastSelectedPlaylistId?.let { id ->
                    binding.playlistsSpinner.selectedItemPosition = playlists
                        .indexOfFirst { it.id == id }
                        .takeIf { it >= 0 } ?: 0
                }
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private suspend fun addToPlaylist(playlistId: String, playlistName: String) {
        val appContext = context?.applicationContext ?: return
        val streams = videoInfo?.let { listOf(it) } ?: PlayingQueue.getStreams()

        val success = try {
            if (streams.isEmpty()) throw IllegalArgumentException()
            PlaylistsHelper.addToPlaylist(playlistId, *streams.toTypedArray())
        } catch (e: Exception) {
            Log.e(TAG(), e.toString())
            appContext.toastFromMainDispatcher(R.string.unknown_error)
            return
        }
        if (success) {
            appContext.toastFromMainDispatcher(
                appContext.getString(R.string.added_to_playlist, playlistName)
            )
        } else {
            appContext.toastFromMainDispatcher(R.string.fail)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        setFragmentResult(ADD_TO_PLAYLIST_DIALOG_DISMISSED_KEY, bundleOf())
    }

    companion object {
        const val ADD_TO_PLAYLIST_DIALOG_DISMISSED_KEY = "add_to_playlist_dialog_dismissed"
    }
}