package com.avinashpatil.app.youtube.ui.models

import android.content.Context
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.api.SubscriptionHelper
import com.avinashpatil.app.youtube.api.obj.StreamItem
import com.avinashpatil.app.youtube.api.obj.Subscription
import com.avinashpatil.app.youtube.extensions.TAG
import com.avinashpatil.app.youtube.extensions.toastFromMainDispatcher
import com.avinashpatil.app.youtube.helpers.PreferenceHelper
import com.avinashpatil.app.youtube.repo.FeedProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubscriptionsViewModel : ViewModel() {
    var videoFeed = MutableLiveData<List<StreamItem>?>()

    var subscriptions = MutableLiveData<List<Subscription>?>()
    val feedProgress = MutableLiveData<FeedProgress?>()

    var subFeedRecyclerViewState: Parcelable? = null

    fun fetchFeed(context: Context, forceRefresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val videoFeed = try {
                SubscriptionHelper.getFeed(forceRefresh = forceRefresh) { feedProgress ->
                    this@SubscriptionsViewModel.feedProgress.postValue(feedProgress)
                }
            } catch (e: Exception) {
                context.toastFromMainDispatcher(R.string.server_error)
                Log.e(TAG(), e.toString())
                return@launch
            }
            this@SubscriptionsViewModel.videoFeed.postValue(videoFeed)
            videoFeed.firstOrNull { !it.isUpcoming }?.uploaded?.let {
                PreferenceHelper.updateLastFeedWatchedTime(it, false)
            }
        }
    }

    fun fetchSubscriptions(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val subscriptions = try {
                SubscriptionHelper.getSubscriptions()
            } catch (e: Exception) {
                context.toastFromMainDispatcher(R.string.server_error)
                Log.e(TAG(), e.toString())
                return@launch
            }
            this@SubscriptionsViewModel.subscriptions.postValue(subscriptions)
        }
    }
}
