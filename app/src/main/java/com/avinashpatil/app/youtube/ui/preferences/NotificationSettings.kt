package com.avinashpatil.app.youtube.ui.preferences

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.SwitchPreferenceCompat
import androidx.work.ExistingPeriodicWorkPolicy
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.constants.PreferenceKeys
import com.avinashpatil.app.youtube.helpers.NotificationHelper
import com.avinashpatil.app.youtube.ui.base.BasePreferenceFragment

class NotificationSettings : BasePreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.notification_settings, rootKey)

        val notificationsEnabled =
            findPreference<SwitchPreferenceCompat>(PreferenceKeys.NOTIFICATION_ENABLED)
        val checkingFrequency = findPreference<ListPreference>(PreferenceKeys.CHECKING_FREQUENCY)
        val requiredNetwork = findPreference<ListPreference>(PreferenceKeys.REQUIRED_NETWORK)

        notificationsEnabled?.setOnPreferenceChangeListener { _, _ ->
            updateNotificationPrefs()
            true
        }

        checkingFrequency?.setOnPreferenceChangeListener { _, _ ->
            updateNotificationPrefs()
            true
        }

        requiredNetwork?.setOnPreferenceChangeListener { _, _ ->
            updateNotificationPrefs()
            true
        }
    }

    private fun updateNotificationPrefs() {
        // replace the previous queued work request
        NotificationHelper
            .enqueueWork(
                context = requireContext(),
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE
            )
    }
}
