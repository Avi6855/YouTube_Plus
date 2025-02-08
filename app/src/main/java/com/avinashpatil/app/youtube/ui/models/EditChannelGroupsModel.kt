package com.avinashpatil.app.youtube.ui.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avinashpatil.app.youtube.db.obj.SubscriptionGroup

class EditChannelGroupsModel : ViewModel() {
    val groups = MutableLiveData<List<SubscriptionGroup>>()
    var groupToEdit: SubscriptionGroup? = null
}
