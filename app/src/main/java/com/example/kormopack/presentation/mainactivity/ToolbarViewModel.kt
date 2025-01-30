package com.example.kormopack.presentation.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToolbarViewModel(): ViewModel() {
    private val _isShowed = MutableLiveData<Boolean>()
    val isShowed: LiveData<Boolean> = _isShowed

    private val _toolbarName = MutableLiveData<String>()
    val toolbarName: LiveData<String> = _toolbarName

    private val _toolbarColor = MutableLiveData<Int>()
    val toolbarColor: LiveData<Int> = _toolbarColor

    fun showToolbar() {
        _isShowed.value = true
    }

    fun hideToolbar() {
        _isShowed.value = false
    }

    fun renameToolbar(name: String) {
        _toolbarName.value = name
    }

    fun changeToolbarColor(colorId: Int) {
        _toolbarColor.value = colorId
    }
}