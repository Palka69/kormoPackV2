package com.example.kormopack.presentation.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawerViewModel: ViewModel() {
    private val _isLocked = MutableLiveData<Boolean>()
    val isLocked: LiveData<Boolean> = _isLocked

    private val _userString = MutableLiveData<String>()
    val userString: LiveData<String> = _userString

    fun lockDrawer() {
        _isLocked.value = true
    }

    fun unlockDrawer() {
        _isLocked.value = false
    }

    fun changeUserString(userName: String) {
        _userString.value = userName
    }
}