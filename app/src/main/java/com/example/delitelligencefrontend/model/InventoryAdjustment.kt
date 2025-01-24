package com.example.delitelligencefrontend.model

import com.example.delitelligencefrontend.enumformodel.AdjustmentType

data class InventoryAdjustment(
    val supplierName: String,
    val productName: String,
    val orderWeight: Double,
    val costPerBox: Double,
    val adjustmentType: AdjustmentType,
    val reason: String

)
