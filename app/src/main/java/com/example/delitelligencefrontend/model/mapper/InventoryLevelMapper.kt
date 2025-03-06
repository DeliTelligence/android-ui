package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAdjustmentDataQuery
import com.example.delitelligence.GetInventoryLevelsQuery
import com.example.delitelligencefrontend.model.InventoryAdjustmentData
import com.example.delitelligencefrontend.model.InventoryLevel

fun GetInventoryLevelsQuery.GetInventoryLevel.toInventoryLevel(): InventoryLevel {
    return InventoryLevel(
        inventoryValue = this.inventoryValue,
        inventoryWeight = this.inventoryWeight,
        productName = this.productName
    )
}