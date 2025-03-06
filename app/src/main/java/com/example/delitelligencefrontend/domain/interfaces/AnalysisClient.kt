package com.example.delitelligencefrontend.domain.interfaces

import com.example.delitelligencefrontend.model.DailySaleData
import com.example.delitelligencefrontend.model.GraphData
import com.example.delitelligencefrontend.model.HandMadeOrNotSalesData
import com.example.delitelligencefrontend.model.InventoryAdjustmentData
import com.example.delitelligencefrontend.model.InventoryLevel
import com.example.delitelligencefrontend.model.QuantitySalesData
import com.example.delitelligencefrontend.model.SalesTypeData

interface AnalysisClient {
    suspend fun salesInAGivenPeriod(startDate: String, endDate: String): List<DailySaleData>
    suspend fun dailySaleTotal(date: String): Double
    suspend fun totalSales(): Double
    suspend fun salesByQuantity(startDate: String, endDate: String): List<QuantitySalesData>
    suspend fun salesByType(startDate: String, endDate: String): List<SalesTypeData>
    suspend fun salesByHandMadeOrNot(startDate: String, endDate: String): List<HandMadeOrNotSalesData>
    suspend fun sendSalesReport(to: String, startDate: String, endDate: String): String
    suspend fun getSalesPrediction(): List<GraphData>
    suspend fun getTotalWeight(): Double
    suspend fun getTotalInventoryValue(): Double
    suspend fun getAdjustmentData(): List<InventoryAdjustmentData>
    suspend fun getInventoryLevels(): List<InventoryLevel>

}