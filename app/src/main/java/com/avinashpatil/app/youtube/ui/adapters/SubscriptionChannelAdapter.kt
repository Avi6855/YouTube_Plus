package com.avinashpatil.app.youtube.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ListAdapter
import com.avinashpatil.app.youtube.api.obj.Subscription
import com.avinashpatil.app.youtube.constants.IntentData
import com.avinashpatil.app.youtube.databinding.ChannelSubscriptionRowBinding
import com.avinashpatil.app.youtube.extensions.toID
import com.avinashpatil.app.youtube.helpers.ImageHelper
import com.avinashpatil.app.youtube.helpers.NavigationHelper
import com.avinashpatil.app.youtube.ui.adapters.callbacks.DiffUtilItemCallback
import com.avinashpatil.app.youtube.ui.base.BaseActivity
import com.avinashpatil.app.youtube.ui.extensions.setupSubscriptionButton
import com.avinashpatil.app.youtube.ui.sheets.ChannelOptionsBottomSheet
import com.avinashpatil.app.youtube.ui.viewholders.SubscriptionChannelViewHolder

class SubscriptionChannelAdapter :
    ListAdapter<Subscription, SubscriptionChannelViewHolder>(DiffUtilItemCallback()) {
    private var visibleCount = 20

    override fun getItemCount() = minOf(visibleCount, currentList.size)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscriptionChannelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ChannelSubscriptionRowBinding.inflate(layoutInflater, parent, false)
        return SubscriptionChannelViewHolder(binding)
    }

    fun updateItems() {
        val oldSize = visibleCount
        visibleCount += minOf(10, currentList.size - oldSize)
        if (visibleCount == oldSize) return
        notifyItemRangeInserted(oldSize, visibleCount)
    }

    override fun onBindViewHolder(holder: SubscriptionChannelViewHolder, position: Int) {
        val subscription = getItem(holder.bindingAdapterPosition)

        holder.binding.apply {
            subscriptionChannelName.text = subscription.name
            ImageHelper.loadImage(subscription.avatar, subscriptionChannelImage, true)

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

            subscriptionSubscribe.setupSubscriptionButton(
                subscription.url.toID(),
                subscription.name,
                notificationBell,
                true
            )
        }
    }
}
