package com.example.delitelligencefrontend.domain.interfaces

import com.example.delitelligence.type.InventoryAdjustmentInputDto
import com.example.delitelligencefrontend.model.Inventory

interface InventoryClient {
    suspend fun getInventory(): List<Inventory>
    suspend fun createAdjustment(input: InventoryAdjustmentInputDto): String?

}