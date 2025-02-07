/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/


package com.example.delitelligencefrontend.data

import com.apollographql.apollo.ApolloClient
import com.example.delitelligence.CreateInventoryAdjustmentMutation
import com.example.delitelligence.GetInventoryQuery
import com.example.delitelligence.type.InventoryAdjustmentInputDto
import com.example.delitelligencefrontend.domain.interfaces.InventoryClient
import com.example.delitelligencefrontend.model.Inventory
import com.example.delitelligencefrontend.model.mapper.toInventory

class ApolloInventoryClient(
    private val apolloClient: ApolloClient

): InventoryClient {

    override suspend fun getInventory(): List<Inventory> {
        return apolloClient
            .query(GetInventoryQuery())
            .execute()
            .data
            ?.getInventory
            ?.mapNotNull{it?.toInventory()}
            ?:emptyList()    }

    override suspend fun createAdjustment(input: InventoryAdjustmentInputDto): String? {
        return try {
            val response = apolloClient
                .mutation(CreateInventoryAdjustmentMutation(input))
                .execute()

            // Assuming response.data?.createSale returns a string
            response.data?.createInventoryAdjustment
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}