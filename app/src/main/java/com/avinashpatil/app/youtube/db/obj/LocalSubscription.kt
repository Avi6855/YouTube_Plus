package com.avinashpatil.app.youtube.db.obj

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.avinashpatil.app.youtube.ui.dialogs.ShareDialog
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "localSubscription")
data class LocalSubscription(
    @PrimaryKey val channelId: String,
    @Ignore val url: String = ""
) {
    constructor(
        channelId: String
    ) : this(channelId, "${ShareDialog.YOUTUBE_FRONTEND_URL}/channel/$channelId")
}
