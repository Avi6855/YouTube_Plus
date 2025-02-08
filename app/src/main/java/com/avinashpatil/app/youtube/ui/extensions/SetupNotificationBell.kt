package com.avinashpatil.app.youtube.ui.extensions

import androidx.core.view.isGone
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.constants.PreferenceKeys
import com.avinashpatil.app.youtube.helpers.PreferenceHelper
import com.google.android.material.button.MaterialButton

fun MaterialButton.setupNotificationBell(channelId: String) {
    if (!PreferenceHelper.getBoolean(PreferenceKeys.NOTIFICATION_ENABLED, true)) {
        isGone = true
        return
    }

    var isIgnorable = PreferenceHelper.isChannelNotificationIgnorable(channelId)
    setIconResource(iconResource(isIgnorable))

    setOnClickListener {
        isIgnorable = !isIgnorable
        PreferenceHelper.toggleIgnorableNotificationChannel(channelId)
        setIconResource(iconResource(isIgnorable))
    }
}

private fun iconResource(isIgnorable: Boolean) =
    if (isIgnorable) R.drawable.ic_bell else R.drawable.ic_notification