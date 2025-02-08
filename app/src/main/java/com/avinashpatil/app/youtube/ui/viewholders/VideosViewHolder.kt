package com.avinashpatil.app.youtube.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.avinashpatil.app.youtube.databinding.AllCaughtUpRowBinding
import com.avinashpatil.app.youtube.databinding.TrendingRowBinding
import com.avinashpatil.app.youtube.databinding.VideoRowBinding

class VideosViewHolder : RecyclerView.ViewHolder {
    var trendingRowBinding: TrendingRowBinding? = null
    var videoRowBinding: VideoRowBinding? = null
    var allCaughtUpBinding: AllCaughtUpRowBinding? = null

    constructor(binding: TrendingRowBinding) : super(binding.root) {
        trendingRowBinding = binding
    }

    constructor(binding: VideoRowBinding) : super(binding.root) {
        videoRowBinding = binding
    }

    constructor(binding: AllCaughtUpRowBinding) : super(binding.root) {
        allCaughtUpBinding = binding
    }
}
