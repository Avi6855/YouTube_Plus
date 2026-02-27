package com.avinashpatil.app.youtube.api

import com.avinashpatil.app.youtube.api.obj.Subscription
import com.avinashpatil.app.youtube.constants.PreferenceKeys
import com.avinashpatil.app.youtube.db.obj.SubscriptionsFeedItem
import com.avinashpatil.app.youtube.helpers.PreferenceHelper
import com.avinashpatil.app.youtube.repo.AccountSubscriptionsRepository
import com.avinashpatil.app.youtube.repo.FeedProgress
import com.avinashpatil.app.youtube.repo.FeedRepository
import com.avinashpatil.app.youtube.repo.LocalFeedRepository
import com.avinashpatil.app.youtube.repo.LocalSubscriptionsRepository
import com.avinashpatil.app.youtube.repo.PipedAccountFeedRepository
import com.avinashpatil.app.youtube.repo.PipedLocalSubscriptionsRepository
import com.avinashpatil.app.youtube.repo.PipedNoAccountFeedRepository
import com.avinashpatil.app.youtube.repo.SubscriptionsRepository

object SubscriptionHelper {
    /**
     * The maximum number of channel IDs that can be passed via a GET request for fetching
     * the subscriptions list and the feed
     */
    const val GET_SUBSCRIPTIONS_LIMIT = 100

    private val localFeedExtraction
        get() = PreferenceHelper.getBoolean(
            PreferenceKeys.LOCAL_FEED_EXTRACTION,
            false
        )
    private val token get() = PreferenceHelper.getToken()
    private val subscriptionsRepository: SubscriptionsRepository
        get() = when {
            token.isNotEmpty() -> AccountSubscriptionsRepository()
            localFeedExtraction -> LocalSubscriptionsRepository()
            else -> PipedLocalSubscriptionsRepository()
        }
    private val feedRepository: FeedRepository
        get() = when {
            localFeedExtraction -> LocalFeedRepository()
            token.isNotEmpty() -> PipedAccountFeedRepository()
            else -> PipedNoAccountFeedRepository()
        }

    suspend fun subscribe(
        channelId: String, name: String, uploaderAvatar: String?, verified: Boolean
    ) = subscriptionsRepository.subscribe(channelId, name, uploaderAvatar, verified)

    suspend fun unsubscribe(channelId: String) {
        subscriptionsRepository.unsubscribe(channelId)
        // remove videos from (local) feed
        feedRepository.removeChannel(channelId)
    }
    suspend fun isSubscribed(channelId: String) = subscriptionsRepository.isSubscribed(channelId)
    suspend fun importSubscriptions(newChannels: List<String>) =
        subscriptionsRepository.importSubscriptions(newChannels)

    suspend fun getSubscriptions() =
        subscriptionsRepository.getSubscriptions().sortedBy { it.name.lowercase() }

    suspend fun getSubscriptionChannelIds() = subscriptionsRepository.getSubscriptionChannelIds()
    suspend fun getFeed(forceRefresh: Boolean, onProgressUpdate: (FeedProgress) -> Unit = {}) =
        feedRepository.getFeed(forceRefresh, onProgressUpdate)

    suspend fun submitFeedItemChange(feedItem: SubscriptionsFeedItem) =
        feedRepository.submitFeedItemChange(feedItem)

    suspend fun submitSubscriptionChannelInfosChanged(subscriptions: List<Subscription>) =
        subscriptionsRepository.submitSubscriptionChannelInfosChanged(subscriptions)
}