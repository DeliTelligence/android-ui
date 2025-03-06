package com.example.delitelligencefrontend.model.mapper


import com.example.delitelligence.GetSalesForecastQuery
import com.example.delitelligencefrontend.model.GraphData

fun GetSalesForecastQuery.GetSalesForecast.toGraphData(): GraphData {
    return GraphData(
        salesTotal = this.salesTotal,
        monthOfSales = this.monthOfSales
    )
}