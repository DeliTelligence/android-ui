package com.example.delitelligencefrontend.model

import com.example.delitelligencefrontend.enumformodel.SaleType

data class DeliSale(
    val employeeId: String,
    val deliProduct: DeliProduct,
    val salePrice: Double,
    val saleWeight: Double,
    val wastePerSale: Double,
    val wastePerSaleValue: Double,
    val differenceWeight: Double,
    val saleType: SaleType,
    val quantity: Int,
    val handMade: Boolean
)
