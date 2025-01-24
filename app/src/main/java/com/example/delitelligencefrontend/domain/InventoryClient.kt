package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.DeliSaleInputDto
import com.example.delitelligence.type.InventoryAdjustmentInputDto
import com.example.delitelligence.type.InventoryFetchDto
import com.example.delitelligencefrontend.model.Inventory

interface InventoryClient {
    suspend fun getInventory(): List<Inventory>
    suspend fun createAdjustment(input: InventoryAdjustmentInputDto): String?

}