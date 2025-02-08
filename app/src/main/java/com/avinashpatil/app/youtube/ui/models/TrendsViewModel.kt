package com.avinashpatil.app.youtube.ui.models

import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avinashpatil.app.youtube.R
import com.avinashpatil.app.youtube.api.RetrofitInstance
import com.avinashpatil.app.youtube.api.obj.StreamItem
import com.avinashpatil.app.youtube.extensions.TAG
import com.avinashpatil.app.youtube.helpers.LocaleHelper
import com.avinashpatil.app.youtube.util.deArrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class TrendsViewModel : ViewModel() {
    val trendingVideos = MutableLiveData<List<StreamItem>>()
    var recyclerViewState: Parcelable? = null

    fun fetchTrending(context: Context) {
        viewModelScope.launch {
            try {
                val region = LocaleHelper.getTrendingRegion(context)
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getTrending(region).deArrow()
                }
                trendingVideos.postValue(response)
            } catch (e: IOException) {
                println(e)
                Log.e(TAG(), "IOException, you might not have internet connection")
                Toast.makeText(context, R.string.unknown_error, Toast.LENGTH_SHORT).show()
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG(), "HttpException, unexpected response")
                Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show()
                return@launch
            }
        }
    }
}
