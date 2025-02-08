package com.avinashpatil.app.youtube.obj

import kotlinx.serialization.Serializable

@Serializable
data class YouTubeWatchHistoryChannelInfo(
    val name: String,
    val url: String
)