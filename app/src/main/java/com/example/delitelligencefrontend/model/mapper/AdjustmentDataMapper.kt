package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAdjustmentDataQuery
import com.example.delitelligence.GetSalesByDateQuery
import com.example.delitelligencefrontend.model.DailySaleData
import com.example.delitelligencefrontend.model.InventoryAdjustmentData

fun GetAdjustmentDataQuery.GetAdjustmentDatum.toAdjustmentData(): InventoryAdjustmentData {
    return InventoryAdjustmentData(
        adjustmentAmount = this.adjustmentAmount,
        adjustmentPercentage = this.adjustmentPercentage,
        adjustmentType = this.adjustmentType
    )
}