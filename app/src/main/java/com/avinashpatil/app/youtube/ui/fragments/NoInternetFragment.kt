package com.avinashpatil.app.youtube.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.databinding.FragmentNointernetBinding
import com.avinashpatil.app.youtube.helpers.NavigationHelper
import com.avinashpatil.app.youtube.helpers.NetworkHelper
import com.avinashpatil.app.youtube.ui.activities.SettingsActivity
import com.google.android.material.snackbar.Snackbar

class NoInternetFragment: Fragment(R.layout.fragment_nointernet) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNointernetBinding.bind(view)
        binding.retryButton.setOnClickListener {
            if (NetworkHelper.isNetworkAvailable(requireContext())) {
                NavigationHelper.restartMainActivity(requireContext())
            } else {
                Snackbar.make(binding.root, R.string.turnInternetOn, Snackbar.LENGTH_LONG).show()
            }
        }
        binding.noInternetSettingsImageView.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.downloads.setOnClickListener {
            findNavController().navigate(R.id.downloadsFragment)
        }
    }
}