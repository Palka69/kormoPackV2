package com.example.kormopack.presentation.fragments.authorizationfrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kormopack.domain.authfrag.CheckStringUseClass

class CheckStringViewModelFactory(
    private val checkStringUseClass: CheckStringUseClass,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckStringViewModel::class.java)) {
            return CheckStringViewModel(checkStringUseClass,) as T
        }
        throw IllegalArgumentException("Невідомий ViewModel-клас")
    }
}