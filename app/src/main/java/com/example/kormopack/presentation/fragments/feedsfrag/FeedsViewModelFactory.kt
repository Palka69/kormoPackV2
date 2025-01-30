package com.example.kormopack.presentation.fragments.feedsfrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kormopack.server.ApiService

class FeedsViewModelFactory(
    private val apiService: ApiService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedsViewModel::class.java)) {
            return FeedsViewModel(apiService,) as T
        }
        throw IllegalArgumentException("Невідомий ViewModel-клас")
    }
}