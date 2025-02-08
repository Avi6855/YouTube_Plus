package com.avinashpatil.app.youtube.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.avinashpatil.app.youtube.databinding.PlaylistBookmarkRowBinding
import com.avinashpatil.app.youtube.databinding.PlaylistsRowBinding

class PlaylistBookmarkViewHolder : RecyclerView.ViewHolder {
    var playlistBookmarkBinding: PlaylistBookmarkRowBinding? = null
    var playlistsBinding: PlaylistsRowBinding? = null

    constructor(binding: PlaylistBookmarkRowBinding) : super(binding.root) {
        playlistBookmarkBinding = binding
    }

    constructor(binding: PlaylistsRowBinding) : super(binding.root) {
        playlistsBinding = binding
    }
}
