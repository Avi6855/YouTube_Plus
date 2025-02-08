package com.avinashpatil.app.youtube.ui.preferences

import android.os.Bundle
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.ui.base.BasePreferenceFragment

class AudioVideoSettings : BasePreferenceFragment() {
    override val titleResourceId: Int = R.string.audio_video

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.audio_video_settings, rootKey)
    }
}
