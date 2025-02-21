package com.example.delitelligencefrontend.data

import com.apollographql.apollo.ApolloClient
import com.example.delitelligence.GetDailySalesTotalQuery
import com.example.delitelligence.GetProductsByTypeQuery
import com.example.delitelligence.GetSalesByDateQuery
import com.example.delitelligence.GetTotalSalesQuery
import com.example.delitelligencefrontend.domain.interfaces.AnalysisClient
import com.example.delitelligencefrontend.model.DailySaleData
import com.example.delitelligencefrontend.model.mapper.toDailySales
import com.example.delitelligencefrontend.model.mapper.toProduct

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

}