package com.example.delitelligencefrontend.model

import com.example.delitelligencefrontend.enumformodel.SaleType

data class SalesTypeData(
    val saleType: SaleType,
    val salePercentageBySaleType: Double,
    val saleAmountSaleType: Double
)
