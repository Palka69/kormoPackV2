package com.example.kormopack.presentation.fragments.personalcabinetfrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kormopack.domain.personalfrag.CalculateWorkDayUseCase
import com.example.kormopack.domain.personalfrag.InitDayChartUseCase
import com.example.kormopack.domain.personalfrag.SheetsDataUseCase

class PersonalViewModelFactory(
    private val sheetsDataUseCase: SheetsDataUseCase,
    private val calculateWorkDayUseCase: CalculateWorkDayUseCase,
    private val initDayChartUseCase: InitDayChartUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalViewModel::class.java)) {
            return PersonalViewModel(sheetsDataUseCase, calculateWorkDayUseCase, initDayChartUseCase) as T
        }
        throw IllegalArgumentException("Невідомий ViewModel-клас")
    }
}
