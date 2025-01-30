package com.example.kormopack.presentation.mainactivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kormopack.domain.authfrag.AuthenticatedModel
import com.example.kormopack.domain.mainact.AuthorizationUseCase
import com.example.kormopack.domain.mainact.CheckAuthUseCase
import com.example.kormopack.domain.mainact.SignOutUseCase
import com.example.kormopack.framework.receivers.NetworkStatusListener
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class MainAuthViewModel(private val checkAuthUseCase: CheckAuthUseCase,
                        private val signOutUseCase: SignOutUseCase,
                        private val authorizationUseCase: AuthorizationUseCase): ViewModel(), NetworkStatusListener {
    private val _isUserAuthenticated = MutableLiveData<AuthenticatedModel>()
    val isUserAuthenticated: LiveData<AuthenticatedModel> = _isUserAuthenticated

    private val _isSignOut = MutableLiveData<Boolean>()
    val isSignOut: LiveData<Boolean> = _isSignOut

    private val _authResult = MutableLiveData<Result<GoogleSignInAccount>>()
    val authResult: LiveData<Result<GoogleSignInAccount>> = _authResult

    private val _networkStatus = MutableLiveData<Boolean>()
    val networkStatus: LiveData<Boolean> = _networkStatus

    fun checkUserAuth() {
        if (_isSignOut.value == true) return
        val result = checkAuthUseCase.execute()
        Log.d("SheetsDataLog", "$result")
        _isUserAuthenticated.value = result.copy()
    }

    fun authenticate(account: GoogleSignInAccount) {
        _authResult.value = authorizationUseCase.signIn(account)
    }

    fun signOut() {
        signOutUseCase.signOut()
    }

    fun setSignOut(bool: Boolean) {
        _isSignOut.value = bool
    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
        _networkStatus.value = isConnected
    }
}