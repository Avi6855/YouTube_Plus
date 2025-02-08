package com.avinashpatil.app.youtube.ui.preferences

import android.os.Bundle
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.ui.base.BasePreferenceFragment

class SponsorBlockSettings : BasePreferenceFragment() {
    override val titleResourceId: Int = R.string.sponsorblock

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.sponsorblock_settings, rootKey)
    }
}
