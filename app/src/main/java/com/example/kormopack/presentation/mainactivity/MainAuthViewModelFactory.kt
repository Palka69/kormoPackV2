package com.example.kormopack.presentation.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kormopack.domain.mainact.AuthorizationUseCase
import com.example.kormopack.domain.mainact.CheckAuthUseCase
import com.example.kormopack.domain.mainact.SignOutUseCase

class MainAuthViewModelFactory(
    private val checkAuthUseCase: CheckAuthUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val authorizationUseCase: AuthorizationUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainAuthViewModel::class.java)) {
            return MainAuthViewModel(checkAuthUseCase, signOutUseCase, authorizationUseCase) as T
        }
        throw IllegalArgumentException("Невідомий ViewModel-клас")
    }
}
