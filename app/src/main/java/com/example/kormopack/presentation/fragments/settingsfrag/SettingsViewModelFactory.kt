package com.example.kormopack.presentation.fragments.settingsfrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kormopack.domain.settingsfrag.AddNewBrandUseCase
import com.example.kormopack.domain.settingsfrag.AddNewFeedUseCase
import com.example.kormopack.domain.settingsfrag.GetAllBrandUseCase

class SettingsViewModelFactory (
    private val addNewBrandUseCase: AddNewBrandUseCase,
    private val addNewFeedUseCase: AddNewFeedUseCase,
    private val getAllBrandUseCase: GetAllBrandUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(addNewBrandUseCase, addNewFeedUseCase, getAllBrandUseCase) as T
        }
        throw IllegalArgumentException("Невідомий ViewModel-клас")
    }
}