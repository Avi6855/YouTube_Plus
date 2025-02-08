package com.avinashpatil.app.youtube.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.content.getSystemService
import com.avinashpatil.app.youtube.enums.NotificationId
import com.avinashpatil.app.youtube.helpers.BackgroundHelper

class OnClearFromRecentService : Service() {
    private var nManager: NotificationManager? = null

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        nManager = getSystemService<NotificationManager>()
        return START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        BackgroundHelper.stopBackgroundPlay(this)
        nManager?.cancel(NotificationId.PLAYER_PLAYBACK.id)
        stopSelf()
    }
}
