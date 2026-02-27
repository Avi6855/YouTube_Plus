package com.avinashpatil.app.youtube.repo

import com.avinashpatil.app.youtube.api.RetrofitInstance
import com.avinashpatil.app.youtube.api.SubscriptionHelper
import com.avinashpatil.app.youtube.api.SubscriptionHelper.GET_SUBSCRIPTIONS_LIMIT
import com.avinashpatil.app.youtube.api.obj.StreamItem

class PipedNoAccountFeedRepository : FeedRepository {
    override suspend fun getFeed(
        forceRefresh: Boolean,
        onProgressUpdate: (FeedProgress) -> Unit
    ): List<StreamItem> {
        val channelIds = SubscriptionHelper.getSubscriptionChannelIds()

        return when {
            channelIds.size > GET_SUBSCRIPTIONS_LIMIT ->
                RetrofitInstance.authApi
                    .getUnauthenticatedFeed(channelIds)

            else -> RetrofitInstance.authApi.getUnauthenticatedFeed(
                channelIds.joinToString(",")
            )
        }
    }
}
