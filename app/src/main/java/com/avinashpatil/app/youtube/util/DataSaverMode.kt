package com.avinashpatil.app.youtube.util

import android.content.Context
import com.avinashpatil.app.youtube.constants.PreferenceKeys
import com.avinashpatil.app.youtube.helpers.NetworkHelper
import com.avinashpatil.app.youtube.helpers.PreferenceHelper

object DataSaverMode {
    fun isEnabled(context: Context): Boolean {
        val pref = PreferenceHelper.getString(PreferenceKeys.DATA_SAVER_MODE, "disabled")
        return when (pref) {
            "enabled" -> true
            "disabled" -> false
            "metered" -> NetworkHelper.isNetworkMetered(context)
            else -> throw IllegalArgumentException()
        }
    }
}
