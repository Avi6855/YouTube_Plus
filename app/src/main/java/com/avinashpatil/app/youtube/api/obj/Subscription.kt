package com.avinashpatil.app.youtube.api.obj

import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    val url: String,
    val name: String,
    val avatar: String? = null,
    val verified: Boolean
)
