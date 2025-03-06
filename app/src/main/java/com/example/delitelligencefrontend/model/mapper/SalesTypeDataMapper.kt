package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAllSalesByTypeQuery
import com.example.delitelligence.GetQuantitySalesQuery
import com.example.delitelligencefrontend.enumformodel.SaleType
import com.example.delitelligencefrontend.enumformodel.StandardType
import com.example.delitelligencefrontend.model.QuantitySalesData
import com.example.delitelligencefrontend.model.SalesTypeData

fun GetAllSalesByTypeQuery.GetSalesByType.toSalesTypeData(): SalesTypeData {
    return SalesTypeData(
        saleType = this.saleType.toSaleType(),
        saleAmountSaleType = this.saleAmountBySaleType,
        salePercentageBySaleType = this.salePercentageBySaleType
    )
}

fun com.example.delitelligence.type.SaleType.toSaleType(): SaleType {
    return when (this) {
        com.example.delitelligence.type.SaleType.COLD_FOOD -> SaleType.COLD_FOOD
        com.example.delitelligence.type.SaleType.SALAD -> SaleType.SALAD
        com.example.delitelligence.type.SaleType.HOT_FOOD -> SaleType.HOT_FOOD
        com.example.delitelligence.type.SaleType.UNKNOWN -> SaleType.UNKNOWN
        com.example.delitelligence.type.SaleType.UNKNOWN__ -> TODO()
    }
}
