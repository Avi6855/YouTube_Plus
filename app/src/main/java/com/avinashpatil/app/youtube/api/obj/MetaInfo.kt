package com.avinashpatil.app.youtube.api.obj

//import kotlinx.serialization.Serializable
/*
Serializable

data class MetaInfo(
    val title: String,
    val description: String,
    val urls: List<String>,
    val urlTexts: List<String>
)

 */
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MetaInfo(
    val title: String,
    val description: String,
    val urls: List<String>,
    val urlTexts: List<String>
): Parcelable
