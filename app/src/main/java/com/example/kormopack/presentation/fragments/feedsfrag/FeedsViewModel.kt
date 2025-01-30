package com.example.kormopack.presentation.fragments.feedsfrag

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kormopack.data.specsdatabase.SpecificationFeedEntity
import com.example.kormopack.server.ApiService
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FeedsViewModel(private val apiService: ApiService): ViewModel() {
    private val _feeds = MutableLiveData<List<SpecificationFeedEntity>>()
    val feeds: LiveData<List<SpecificationFeedEntity>> = _feeds

    fun fetchFeeds(brand: String) {
        viewModelScope.launch {
            try {
                val result = apiService.getFeeds(brand)
                _feeds.postValue(result)
            } catch (e: Exception) {
                Log.e("FeedsViewModel", "", e)
            }
        }
    }

    fun fetchFeedsWhichContains(brand: String, keyWord: String) {
        viewModelScope.launch {
            try {
                val result = apiService.getFeeds(brand, keyWord)
                _feeds.postValue(result)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.d("FeedsViewModel", "Не знайдено специфікацію бренду: $brand з ключем: $keyWord")
                    _feeds.postValue(emptyList())
                } else {
                    Log.e("FeedsViewModel", "Помилка пошуку брендів", e)
                }
            } catch (e: Exception) {
                Log.e("FeedsViewModel", "Помилка", e)
            }
        }
    }
}