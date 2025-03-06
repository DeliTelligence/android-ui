package com.example.delitelligencefrontend.data

import com.apollographql.apollo.ApolloClient
import com.example.delitelligence.GetAdjustmentDataQuery
import com.example.delitelligence.GetAllSalesByHandMadeOrNotQuery
import com.example.delitelligence.GetAllSalesByTypeQuery
import com.example.delitelligence.GetDailySalesTotalQuery
import com.example.delitelligence.GetInventoryLevelsQuery
import com.example.delitelligence.GetQuantitySalesQuery
import com.example.delitelligence.GetSalesByDateQuery
import com.example.delitelligence.GetSalesForecastQuery
import com.example.delitelligence.GetTotalInventoryValueQuery
import com.example.delitelligence.GetTotalSalesQuery
import com.example.delitelligence.GetTotalWeightQuery
import com.example.delitelligence.PostSendSalesReportMutation
import com.example.delitelligencefrontend.domain.interfaces.AnalysisClient
import com.example.delitelligencefrontend.model.DailySaleData
import com.example.delitelligencefrontend.model.GraphData
import com.example.delitelligencefrontend.model.HandMadeOrNotSalesData
import com.example.delitelligencefrontend.model.InventoryAdjustmentData
import com.example.delitelligencefrontend.model.InventoryLevel
import com.example.delitelligencefrontend.model.QuantitySalesData
import com.example.delitelligencefrontend.model.SalesTypeData
import com.example.delitelligencefrontend.model.mapper.toAdjustmentData
import com.example.delitelligencefrontend.model.mapper.toDailySales
import com.example.delitelligencefrontend.model.mapper.toGraphData
import com.example.delitelligencefrontend.model.mapper.toHandMadeSales
import com.example.delitelligencefrontend.model.mapper.toInventoryLevel
import com.example.delitelligencefrontend.model.mapper.toQuantitySalesData
import com.example.delitelligencefrontend.model.mapper.toSalesTypeData

class ApolloAnalysisClient(
    private val apolloClient: ApolloClient
): AnalysisClient {
    override suspend fun salesInAGivenPeriod(startDate: String, endDate: String): List<DailySaleData> {
        return apolloClient
            .query(GetSalesByDateQuery(startDate, endDate))
            .execute()
            .data
            ?.getAllSalesByDate
            ?.mapNotNull { it?.toDailySales() }
            ?: emptyList()
    }

    override suspend fun dailySaleTotal(date: String): Double {
        val response = apolloClient
            .query(GetDailySalesTotalQuery(date))
            .execute()

        // Access the data from the response
        val totalSales: Double? = response.data?.dailySalesTotal

        return totalSales ?: 0.0  // Return 0.0 if the response is null as a default case
    }

    override suspend fun totalSales(): Double {
        val response = apolloClient
            .query(GetTotalSalesQuery())
            .execute()

        // Debugging Logs
        println("GraphQL Response: $response")

        // Check if there are errors
        if (response.hasErrors()) {
            println("GraphQL Errors: ${response.errors}")
            return 0.0  // Return default or handle errors properly
        }

        // Access the data from the response
        val totalSales: Double? = response.data?.totalSales
        println("Total Sales Retrieved: $totalSales")

        return totalSales ?: 0.0  // Return 0.0 if the response is null
    }

    override suspend fun salesByQuantity(startDate: String, endDate: String): List<QuantitySalesData> {
        return apolloClient
            .query(GetQuantitySalesQuery(startDate, endDate))
            .execute()
            .data
            ?.getSalesByQuantity
            ?.mapNotNull { it?.toQuantitySalesData() }
            ?: emptyList()    }

    override suspend fun salesByType(startDate: String, endDate: String): List<SalesTypeData> {
        return apolloClient
            .query(GetAllSalesByTypeQuery(startDate, endDate))
            .execute()
            .data
            ?.getSalesByType
            ?.mapNotNull { it?.toSalesTypeData() }
            ?: emptyList()     }

    override suspend fun salesByHandMadeOrNot(
        startDate: String,
        endDate: String
    ): List<HandMadeOrNotSalesData> {
        return apolloClient
            .query(GetAllSalesByHandMadeOrNotQuery(startDate, endDate))
            .execute()
            .data
            ?.getHandMadeOrNotSalesData
            ?.mapNotNull { it?.toHandMadeSales() }
            ?: emptyList()     }

    override suspend fun sendSalesReport(to: String, startDate: String, endDate: String): String {
        return try {
            val response = apolloClient
                .mutation(PostSendSalesReportMutation(to, startDate, endDate))
                .execute()

            // Assuming response.data?.createSale returns a string
            response.data?.sendReport.toString()
        } catch (e: Exception) {
            e.printStackTrace().toString()

        }
    }

    override suspend fun getSalesPrediction(): List<GraphData> {
        return apolloClient
            .query(GetSalesForecastQuery())
            .execute()
            .data
            ?.getSalesForecast
            ?.filterNotNull()
            ?.map { it.toGraphData() }
            ?: emptyList()
    }

    override suspend fun getTotalWeight(): Double {
        val response = apolloClient
            .query(GetTotalWeightQuery())
            .execute()

        // Debugging Logs
        println("GraphQL Response: $response")

        // Check if there are errors
        if (response.hasErrors()) {
            println("GraphQL Errors: ${response.errors}")
            return 0.0  // Return default or handle errors properly
        }

        // Access the data from the response
        val totalWeight: Double? = response.data?.getTotalWeight
        println("Total Weight Retrieved: $totalWeight")

        return totalWeight ?: 0.0
    }

    override suspend fun getTotalInventoryValue(): Double {
        val response = apolloClient
            .query(GetTotalInventoryValueQuery())
            .execute()

        // Debugging Logs
        println("GraphQL Response: $response")

        // Check if there are errors
        if (response.hasErrors()) {
            println("GraphQL Errors: ${response.errors}")
            return 0.0  // Return default or handle errors properly
        }

        // Access the data from the response
        val totalInventoryValue: Double? = response.data?.getTotalInventoryValue
        println("Total Value Retrieved: $totalInventoryValue")

        return totalInventoryValue ?: 0.0    }

    override suspend fun getAdjustmentData(): List<InventoryAdjustmentData> {
        return apolloClient
            .query(GetAdjustmentDataQuery())
            .execute()
            .data
            ?.getAdjustmentData
            ?.filterNotNull()
            ?.map { it.toAdjustmentData() }
            ?: emptyList()    }

    override suspend fun getInventoryLevels(): List<InventoryLevel> {
        return apolloClient
            .query(GetInventoryLevelsQuery())
            .execute()
            .data
            ?.getInventoryLevels
            ?.filterNotNull()
            ?.map { it.toInventoryLevel() }
            ?: emptyList()      }

}