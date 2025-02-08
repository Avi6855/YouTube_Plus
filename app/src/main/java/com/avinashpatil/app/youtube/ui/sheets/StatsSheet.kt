package com.avinashpatil.app.youtube.ui.sheets

import android.os.Bundle
import android.view.View
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.databinding.DialogStatsBinding
import com.avinashpatil.app.youtube.extensions.parcelable
import com.avinashpatil.app.youtube.helpers.ClipboardHelper
import com.avinashpatil.app.youtube.obj.VideoStats

class StatsSheet : ExpandedBottomSheet(R.layout.dialog_stats) {
    private lateinit var stats: VideoStats

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stats = arguments?.parcelable(IntentData.videoStats)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = DialogStatsBinding.bind(view)
        binding.videoId.setText(stats.videoId)
        binding.videoIdCopy.setEndIconOnClickListener {
            ClipboardHelper.save(requireContext(), "text", stats.videoId)
        }
        binding.videoInfo.setText(stats.videoInfo)
        binding.audioInfo.setText(stats.audioInfo)
        binding.videoQuality.setText(stats.videoQuality)
    }
}
