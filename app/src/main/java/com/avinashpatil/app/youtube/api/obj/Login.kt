package com.avinashpatil.app.youtube.api.obj

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val username: String,
    val password: String
)
