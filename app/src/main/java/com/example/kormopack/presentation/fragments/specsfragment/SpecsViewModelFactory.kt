package com.example.kormopack.presentation.fragments.specsfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kormopack.server.ApiService

class SpecsViewModelFactory(
    private val apiService: ApiService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpecsViewModel::class.java)) {
            return SpecsViewModel(apiService,) as T
        }
        throw IllegalArgumentException("Невідомий ViewModel-клас")
    }
}