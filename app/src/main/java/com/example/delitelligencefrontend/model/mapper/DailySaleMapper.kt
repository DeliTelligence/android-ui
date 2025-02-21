package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetSalesByDateQuery
import com.example.delitelligencefrontend.model.DailySaleData

fun GetSalesByDateQuery.GetAllSalesByDate.toDailySales(): DailySaleData {
    return DailySaleData(
        salesPercentage = this.salePercentage,
        saleCategoryTime = this.saleCategoryTime,
        saleAmount = this.saleAmount
    )
}