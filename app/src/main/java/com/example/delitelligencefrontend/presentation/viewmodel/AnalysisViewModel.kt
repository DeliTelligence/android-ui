/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.domain.AnalysisUseCase
import com.example.delitelligencefrontend.domain.EmployeesUseCase
import com.example.delitelligencefrontend.model.DailySaleData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val analysisUseCase: AnalysisUseCase
) : ViewModel() {

    private val _totalSales = mutableStateOf(0.0)
    val totalSales: State<Double> get() = _totalSales

    private val _totalSalesDay = mutableStateOf(0.0)
    val totalSalesDay: State<Double> get() = _totalSalesDay

    private val _salesInGivenPeriod = mutableStateOf<List<DailySaleData>>(emptyList())
    val salesInGivenPeriod: State<List<DailySaleData>> get() = _salesInGivenPeriod

    fun loadTotalSalesDay(date: String) {
        viewModelScope.launch {
            _totalSalesDay.value = analysisUseCase.executeDailySalesTotal(date)
        }
    }

    fun loadTotalSales(){
        viewModelScope.launch {
            _totalSales.value = analysisUseCase.execute()
        }
    }


    fun getSalesInGivenPeriod(startDate: String, endDate: String) {
        viewModelScope.launch {
            _salesInGivenPeriod.value = analysisUseCase.execute(startDate, endDate)
        }
    }
}
