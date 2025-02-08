package com.avinashpatil.app.youtube.obj

import android.graphics.Bitmap
import com.avinashpatil.app.youtube.api.obj.Streams

data class DownloadedFile(
    val name: String,
    val size: Long,
    var metadata: Streams? = null,
    var thumbnail: Bitmap? = null
)
