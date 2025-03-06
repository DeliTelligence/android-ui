package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAllSalesByHandMadeOrNotQuery
import com.example.delitelligence.GetQuantitySalesQuery
import com.example.delitelligencefrontend.model.HandMadeOrNotSalesData
import com.example.delitelligencefrontend.model.QuantitySalesData

fun GetAllSalesByHandMadeOrNotQuery.GetHandMadeOrNotSalesDatum.toHandMadeSales(): HandMadeOrNotSalesData {
    return HandMadeOrNotSalesData(
        saleAmountHandMade = this.saleAmountHandMade,
        handMadeDescription = this.handMadeDescription,
        saleHandMadeByPercentage = this.saleHandMadeByPercentage
    )
}

