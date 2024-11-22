package com.example.delitelligencefrontend.model

import com.example.delitelligencefrontend.enumformodel.SaleType

data class DeliSale(
    val employeeId: String,
    val salePrice: Float,
    val saleWeight: Float,
    val wastePerSale: Float,
    val wastePerSaleValue: Float,
    val differenceWeight: Float,
    val saleType: SaleType,
    val saleQuantity: Int,
    val deliProduct: List<DeliProduct>
)