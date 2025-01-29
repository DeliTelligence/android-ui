/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.data

import com.apollographql.apollo.ApolloClient
import com.example.delitelligence.CreateDeliSaleMutation
import com.example.delitelligence.type.DeliSaleInputDto
import com.example.delitelligencefrontend.domain.interfaces.SaleClient

class ApolloSaleClient(
    private val apolloClient: ApolloClient

): SaleClient {
    override suspend fun createSale(input: DeliSaleInputDto): String? {
        return try {
            val response = apolloClient
                .mutation(CreateDeliSaleMutation(input))
                .execute()

            // Assuming response.data?.createSale returns a string
            response.data?.createSale
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}