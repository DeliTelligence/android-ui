package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetQuantitySalesQuery
import com.example.delitelligence.GetSalesByDateQuery
import com.example.delitelligencefrontend.model.DailySaleData
import com.example.delitelligencefrontend.model.QuantitySalesData

fun GetQuantitySalesQuery.GetSalesByQuantity.toQuantitySalesData(): QuantitySalesData {
    return QuantitySalesData(
        salesData = this.salesData,
        quantityRepresented = this.quantityRepresented,
        percentage = this.percentage
    )
}