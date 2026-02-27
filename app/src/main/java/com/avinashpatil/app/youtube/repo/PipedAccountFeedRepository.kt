package com.avinashpatil.app.youtube.repo

import com.avinashpatil.app.youtube.api.RetrofitInstance
import com.avinashpatil.app.youtube.api.obj.StreamItem
import com.avinashpatil.app.youtube.helpers.PreferenceHelper

class PipedAccountFeedRepository : FeedRepository {
    override suspend fun getFeed(
        forceRefresh: Boolean,
        onProgressUpdate: (FeedProgress) -> Unit
    ): List<StreamItem> {
        val token = PreferenceHelper.getToken()

        return RetrofitInstance.authApi.getFeed(token)
    }
}