/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 10/10/2024 accessed
All code here is adapted from the video*/
package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.InventoryAdjustmentInputDto
import com.example.delitelligencefrontend.domain.interfaces.InventoryClient
import com.example.delitelligencefrontend.model.Inventory

class InventoryUseCase(
    private val inventoryClient: InventoryClient

) {

    suspend fun execute(): List<Inventory> {
        return inventoryClient.getInventory()
    }
    suspend fun execute(input: InventoryAdjustmentInputDto): String? {
        return inventoryClient.createAdjustment(input)
    }

}