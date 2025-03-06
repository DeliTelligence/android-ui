/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.domain.AnalysisUseCase
import com.example.delitelligencefrontend.domain.EmployeesUseCase
import com.example.delitelligencefrontend.model.DailySaleData
import com.example.delitelligencefrontend.model.GraphData
import com.example.delitelligencefrontend.model.HandMadeOrNotSalesData
import com.example.delitelligencefrontend.model.InventoryAdjustmentData
import com.example.delitelligencefrontend.model.InventoryLevel
import com.example.delitelligencefrontend.model.QuantitySalesData
import com.example.delitelligencefrontend.model.SalesTypeData
import com.example.delitelligencefrontend.model.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val analysisUseCase: AnalysisUseCase,
    private val session: Session
) : ViewModel() {

    private val _totalSales = mutableStateOf(0.0)
    val totalSales: State<Double> get() = _totalSales

    private val _totalSalesDay = mutableStateOf(0.0)
    val totalSalesDay: State<Double> get() = _totalSalesDay

    private val _salesInGivenPeriod = mutableStateOf<List<DailySaleData>>(emptyList())
    val salesInGivenPeriod: State<List<DailySaleData>> get() = _salesInGivenPeriod

    private val _salesByQuantityData = mutableStateOf<List<QuantitySalesData>>(emptyList())
    val salesByQuantityData: State<List<QuantitySalesData>> get() = _salesByQuantityData

    private val _salesByTypeData = mutableStateOf<List<SalesTypeData>>(emptyList())
    val salesByTypeData: State<List<SalesTypeData>> get() = _salesByTypeData

    private val _salesByHandMadeOrNotData = mutableStateOf<List<HandMadeOrNotSalesData>>(emptyList())
    val salesByHandMadeOrNotData: State<List<HandMadeOrNotSalesData>> get() = _salesByHandMadeOrNotData

    private val _salesPredictions = mutableStateOf<List<GraphData>>(emptyList())
    val salesPredictions: State<List<GraphData>> get() = _salesPredictions

    private val _reportSendingStatus = mutableStateOf<ReportSendingStatus>(ReportSendingStatus.Idle)
    val reportSendingStatus: State<ReportSendingStatus> get() = _reportSendingStatus

    private val _totalInventoryValue = mutableStateOf(0.0)
    val totalInventoryValue: State<Double> = _totalInventoryValue

    private val _totalInventoryWeight = mutableStateOf(0.0)
    val totalInventoryWeight: State<Double> = _totalInventoryWeight

    private val _inventoryAdjustments = mutableStateOf<List<InventoryAdjustmentData>>(emptyList())
    val inventoryAdjustments: State<List<InventoryAdjustmentData>> = _inventoryAdjustments

    private val _inventoryLevels = mutableStateOf<List<InventoryLevel>>(emptyList())
    val inventoryLevels: State<List<InventoryLevel>> = _inventoryLevels

    fun loadTotalSalesDay(date: String) {
        viewModelScope.launch {
            _totalSalesDay.value = analysisUseCase.executeDailySalesTotal(date)
        }
    }

    fun loadTotalSales() {
        viewModelScope.launch {
            _totalSales.value = analysisUseCase.execute()
        }
    }

    fun getSalesInGivenPeriod(startDate: String, endDate: String) {
        viewModelScope.launch {
            _salesInGivenPeriod.value = analysisUseCase.execute(startDate, endDate)
        }
    }

    fun getQuantityOfSalesData(startDate: String, endDate: String) {
        viewModelScope.launch {
            _salesByQuantityData.value = analysisUseCase.executeSaleByQuantity(startDate, endDate)
        }
    }

    fun getSalesByTypeData(startDate: String, endDate: String) {
        viewModelScope.launch {
            _salesByTypeData.value = analysisUseCase.executeSaleByType(startDate, endDate)
        }
    }

    fun getSalesByHandMadeOrNot(startDate: String, endDate: String) {
        viewModelScope.launch {
            _salesByHandMadeOrNotData.value = analysisUseCase.executeSaleByHandMadeOrNot(startDate, endDate)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendSalesReport(to: String, startDate: String, endDate: String) {
        val formattedStartDate = formatStringDate(startDate)
        val formattedEndDate = formatStringDate(endDate)

        viewModelScope.launch {
            _reportSendingStatus.value = ReportSendingStatus.Sending
            try {
                if (formattedStartDate != null && (endDate.isEmpty() || formattedEndDate != null)) {
                    val result = analysisUseCase.executeSendReport(to, formattedStartDate, formattedEndDate ?: "")
                    if (result == "Report Sent") {
                        _reportSendingStatus.value = ReportSendingStatus.Success("Report Sent")
                    } else {
                        _reportSendingStatus.value = ReportSendingStatus.Error("Report Not Sent")
                    }
                } else {
                    _reportSendingStatus.value = ReportSendingStatus.Error("Invalid date format. Please use YYYY-MM-DD.")
                }
            } catch (e: Exception) {
                _reportSendingStatus.value = ReportSendingStatus.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun getSalesPrediction() {
        viewModelScope.launch {
            _salesPredictions.value = analysisUseCase.executeSalesPrediction()
        }
    }
    fun loadInventoryData() {
        viewModelScope.launch {
            _totalInventoryValue.value = analysisUseCase.executeGetTotalValue()
            _totalInventoryWeight.value = analysisUseCase.executeGetTotalWeight()
            _inventoryAdjustments.value = analysisUseCase.executeGetAdjustmentData()
            _inventoryLevels.value = analysisUseCase.executeGetInventoryLevelData()
        }
    }

    fun formatAndValidateDate(date: Any): String? = when (date) {
        is String -> formatStringDate(date)
        is LocalDate -> formatLocalDate(date)
        else -> null
    }

    fun formatStringDate(dateStr: String): String? {
        if (dateStr.isEmpty()) return null

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return try {
            LocalDate.parse(dateStr, formatter).format(formatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun formatLocalDate(localDate: LocalDate): String =
        localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))


    fun getEmployeeEmail(): String? {
        return session.getUser()?.employeeEmail
    }
}



sealed class ReportSendingStatus {
    object Idle : ReportSendingStatus()
    object Sending : ReportSendingStatus()
    data class Success(val message: String) : ReportSendingStatus()
    data class Error(val message: String) : ReportSendingStatus()
}