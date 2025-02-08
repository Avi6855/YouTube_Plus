package com.avinashpatil.app.youtube.repo

import com.avinashpatil.app.youtube.api.obj.StreamItem

interface FeedRepository {
    suspend fun getFeed(forceRefresh: Boolean): List<StreamItem>
}