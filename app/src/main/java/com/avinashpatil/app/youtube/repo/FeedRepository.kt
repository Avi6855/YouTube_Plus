package com.avinashpatil.app.youtube.repo

import com.avinashpatil.app.youtube.api.obj.StreamItem
import com.avinashpatil.app.youtube.db.obj.SubscriptionsFeedItem

data class FeedProgress(
    val currentProgress: Int,
    val total: Int
)

interface FeedRepository {
    suspend fun getFeed(
        forceRefresh: Boolean,
        onProgressUpdate: (FeedProgress) -> Unit
    ): List<StreamItem>
    suspend fun removeChannel(channelId: String) {}
    suspend fun submitFeedItemChange(feedItem: SubscriptionsFeedItem) {}
}