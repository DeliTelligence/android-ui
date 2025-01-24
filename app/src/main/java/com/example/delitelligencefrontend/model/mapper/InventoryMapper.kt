package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAllProductsQuery
import com.example.delitelligence.GetInventoryQuery
import com.example.delitelligencefrontend.model.Inventory
import com.example.delitelligencefrontend.model.Product

fun GetInventoryQuery.GetInventory.toInventory(): Inventory {
    // Provide default values if any of the fields could be null
    return Inventory(
        productName = this.productName ?: "Unknown", // Default to 'Unknown' if null
        totalWeight = this.totalWeight ?: 0.0, // Default to 0.0 if null
        location = this.location ?: "Unknown location", // Default to 'Unknown location' if null
        inventoryValue = this.inventoryValue ?: 0.0 // Default to 0.0 if null
    )
}