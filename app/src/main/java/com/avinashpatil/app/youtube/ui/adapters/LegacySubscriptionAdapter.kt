package com.avinashpatil.app.youtube.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ListAdapter
import com.avinashpatil.app.youtube.api.obj.Subscription
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.databinding.LegacySubscriptionChannelBinding
import com.avinashpatil.app.youtube.extensions.toID
import com.avinashpatil.app.youtube.helpers.ImageHelper
import com.avinashpatil.app.youtube.helpers.NavigationHelper
import com.avinashpatil.app.youtube.ui.adapters.callbacks.DiffUtilItemCallback
import com.avinashpatil.app.youtube.ui.base.BaseActivity
import com.avinashpatil.app.youtube.ui.sheets.ChannelOptionsBottomSheet
import com.avinashpatil.app.youtube.ui.viewholders.LegacySubscriptionViewHolder

class LegacySubscriptionAdapter :
    ListAdapter<Subscription, LegacySubscriptionViewHolder>(DiffUtilItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LegacySubscriptionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LegacySubscriptionChannelBinding.inflate(layoutInflater, parent, false)
        return LegacySubscriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LegacySubscriptionViewHolder, position: Int) {
        val subscription = getItem(holder.bindingAdapterPosition)
        holder.binding.apply {
            channelName.text = subscription.name
            ImageHelper.loadImage(
                subscription.avatar,
                channelAvatar,
                true
            )
            root.setOnClickListener {
                NavigationHelper.navigateChannel(root.context, subscription.url)
            }

            root.setOnLongClickListener {
                val channelOptionsSheet = ChannelOptionsBottomSheet()
                channelOptionsSheet.arguments = bundleOf(
                    IntentData.channelId to subscription.url.toID(),
                    IntentData.channelName to subscription.name,
                    IntentData.isSubscribed to true
                )
                channelOptionsSheet.show((root.context as BaseActivity).supportFragmentManager)
                true
            }
        }
    }
}