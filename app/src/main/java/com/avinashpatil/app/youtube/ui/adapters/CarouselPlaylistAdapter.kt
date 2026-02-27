package com.avinashpatil.app.youtube.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ListAdapter
import com.avinashpatil.app.youtube.api.PlaylistsHelper
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.databinding.CarouselPlaylistThumbnailBinding
import com.avinashpatil.app.youtube.helpers.ImageHelper
import com.avinashpatil.app.youtube.helpers.NavigationHelper
import com.avinashpatil.app.youtube.ui.adapters.callbacks.DiffUtilItemCallback
import com.avinashpatil.app.youtube.ui.base.BaseActivity
import com.avinashpatil.app.youtube.ui.sheets.PlaylistOptionsBottomSheet
import com.avinashpatil.app.youtube.ui.viewholders.CarouselPlaylistViewHolder

data class CarouselPlaylist(
    val id: String,
    val title: String?,
    val thumbnail: String?
)

class CarouselPlaylistAdapter : ListAdapter<CarouselPlaylist, CarouselPlaylistViewHolder>(
    DiffUtilItemCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarouselPlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CarouselPlaylistViewHolder(CarouselPlaylistThumbnailBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(
        holder: CarouselPlaylistViewHolder,
        position: Int
    ) {
        val item = getItem(position)!!

        with(holder.binding) {
            playlistName.text = item.title
            ImageHelper.loadImage(item.thumbnail, thumbnail)

            val type = PlaylistsHelper.getPlaylistType(item.id)
            root.setOnClickListener {
                NavigationHelper.navigatePlaylist(root.context, item.id, type)
            }

            root.setOnLongClickListener {
                val playlistOptionsDialog = PlaylistOptionsBottomSheet()
                playlistOptionsDialog.arguments = bundleOf(
                    IntentData.playlistId to item.id,
                    IntentData.playlistName to item.title,
                    IntentData.playlistType to type
                )
                playlistOptionsDialog.show((root.context as BaseActivity).supportFragmentManager)

                true
            }
        }
    }
}