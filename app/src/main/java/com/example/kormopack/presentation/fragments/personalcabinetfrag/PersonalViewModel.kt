package com.example.kormopack.presentation.fragments.personalcabinetfrag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kormopack.domain.personalfrag.CalculateWorkDayUseCase
import com.example.kormopack.domain.personalfrag.InitDayChartUseCase
import com.example.kormopack.domain.personalfrag.SheetsDataUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

class PersonalViewModel(private val sheetsDataUseCase: SheetsDataUseCase,
                        private val calculateWorkDayUseCase: CalculateWorkDayUseCase,
                        private val initDayChartUseCase: InitDayChartUseCase
) : ViewModel() {

    private val _sheetsData = MutableLiveData<List<List<Any>>?>()
    val sheetsData: LiveData<List<List<Any>>?> get() = _sheetsData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _workDays = MutableLiveData<MutableList<MutableList<Any>>>()
    val workDays: LiveData<MutableList<MutableList<Any>>> = _workDays

    private val _dayChartData = MutableLiveData<MutableMap<String, Float>>()
    val dayChartData: LiveData<MutableMap<String, Float>> = _dayChartData

    fun fetchSheetsData(account: GoogleSignInAccount) {
        viewModelScope.launch {
            try {
                val data = sheetsDataUseCase.execute(account)
                _sheetsData.postValue(data)
            } catch (e: Exception) {
                _error.postValue("Помилка отримання даних: ${e.message}")
            }
        }
    }

    fun getWorkDays(cash: List<List<Any>>, userName: String) {
        _workDays.value = calculateWorkDayUseCase.execute(cash, userName)
    }

    fun initDayChart(workDays: MutableList<MutableList<Any>>, bonusMap: MutableMap<Int, Float>) {
        _dayChartData.value = initDayChartUseCase.execute(workDays, bonusMap)
    }
}
