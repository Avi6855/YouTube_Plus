package com.avinashpatil.app.youtube.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.avinashpatil.app.youtube.databinding.AllCaughtUpRowBinding
import com.avinashpatil.app.youtube.databinding.TrendingRowBinding

class VideoCardsViewHolder : RecyclerView.ViewHolder {
    var trendingRowBinding: TrendingRowBinding? = null
    var allCaughtUpBinding: AllCaughtUpRowBinding? = null

    constructor(binding: TrendingRowBinding) : super(binding.root) {
        trendingRowBinding = binding
    }

    constructor(binding: AllCaughtUpRowBinding) : super(binding.root) {
        allCaughtUpBinding = binding
    }
}
