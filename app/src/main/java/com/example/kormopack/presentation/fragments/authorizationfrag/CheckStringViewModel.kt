package com.example.kormopack.presentation.fragments.authorizationfrag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kormopack.domain.authfrag.CheckStringUseClass

class CheckStringViewModel(
    private val checkStringUseClass: CheckStringUseClass,
) : ViewModel() {
    private val _regexString = MutableLiveData<List<Boolean>>()
    val regexString: LiveData<List<Boolean>> = _regexString

    fun checkString(string: String) {
        _regexString.value = checkStringUseClass.execute(string)
    }
}