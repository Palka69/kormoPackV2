package com.example.kormopack.presentation.fragments.specsfragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kormopack.data.specsdatabase.SpecificationBrandEntity
import com.example.kormopack.server.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SpecsViewModel(private val apiService: ApiService):ViewModel() {
    private val _brands = MutableLiveData<List<SpecificationBrandEntity>>()
    val brands: LiveData<List<SpecificationBrandEntity>> = _brands

    fun fetchFeedBrands() {
        viewModelScope.launch {
            try {
                delay(320)
                val result = apiService.getFeedBrands()
                _brands.postValue(result)
            } catch (e: Exception) {
                Log.e("SpecsViewModel", "Error fetching players", e)
            }
        }
    }
}