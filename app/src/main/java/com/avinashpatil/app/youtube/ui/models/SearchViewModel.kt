package com.avinashpatil.app.youtube.ui.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    val searchQuery = MutableLiveData<String>()
    fun setQuery(query: String?) {
        this.searchQuery.value = query
    }
}
