package com.avinashpatil.app.youtube.api.obj

import kotlinx.serialization.Serializable

@Serializable
data class DeleteUserRequest(val password: String)
