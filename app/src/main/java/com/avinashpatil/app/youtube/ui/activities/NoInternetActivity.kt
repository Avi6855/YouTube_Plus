package com.avinashpatil.app.youtube.ui.activities

import android.content.Intent
import android.os.Bundle
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.databinding.ActivityNointernetBinding
import com.avinashpatil.app.youtube.helpers.NavigationHelper
import com.avinashpatil.app.youtube.ui.base.BaseActivity

class NoInternetActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNointernetBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.getBooleanExtra(IntentData.maximizePlayer, false)) {
            NavigationHelper.openAudioPlayerFragment(this, offlinePlayer = true)
        }
    }
}