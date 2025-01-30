package com.example.kormopack.presentation.fragments.settingsfrag

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kormopack.domain.settingsfrag.AddNewBrandUseCase
import com.example.kormopack.domain.settingsfrag.AddNewFeedUseCase
import com.example.kormopack.domain.settingsfrag.GetAllBrandUseCase
import com.example.kormopack.domain.specsfrag.model.SpecificationBrandModel
import com.example.kormopack.domain.specsfrag.model.SpecificationFeedModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewModel(private val addNewBrandUseCase: AddNewBrandUseCase,
                        private val addNewFeedUseCase: AddNewFeedUseCase,
    private val getAllBrandUseCase: GetAllBrandUseCase
): ViewModel() {
    private val _brandAdded = MutableLiveData<Boolean>()
    val brandAdded: LiveData<Boolean> = _brandAdded

    private val _feedAdded = MutableLiveData<Boolean>()
    val feedAdded: LiveData<Boolean> = _feedAdded

    private val _allBrandList = MutableLiveData<List<SpecificationBrandModel>>()
    val allBrandList: MutableLiveData<List<SpecificationBrandModel>> = _allBrandList

    fun addNewBrand(brand: String) {
        viewModelScope.launch {
            try {
                delay(10)
                val res = addNewBrandUseCase.addNewBrand(brand)
                if (res?.isSuccessful == true) {
                    _brandAdded.postValue(true)
                } else {
                    _brandAdded.postValue(false)
                }
            } catch (e: Exception) {
                Log.e("SpecsViewModel", "Помилка додавання бренду", e)
            }
        }
    }

    fun addNewSpec(spec: SpecificationFeedModel) {
        viewModelScope.launch {
            try {
                delay(10)
                val res = addNewFeedUseCase.addNewFeed(spec)
                if (res?.isSuccessful == true) {
                    _feedAdded.postValue(true)
                } else {
                    _feedAdded.postValue(false)
                }
            } catch (e: Exception) {
                Log.e("SpecsViewModel", "Помилка додавання специфікації", e)
            }
        }
    }

    fun getAllBrands() {
        viewModelScope.launch {
            try {
                delay(10)
                _allBrandList.postValue(getAllBrandUseCase.getAllBrands())
            } catch (e: Exception) {
                Log.e("SpecsViewModel", "Помилка повернення усіх брендів", e)
            }
        }
    }
}