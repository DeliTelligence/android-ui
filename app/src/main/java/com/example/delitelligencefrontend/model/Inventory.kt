package com.example.delitelligencefrontend.model

data class Inventory(
    val products: List<InventoryProduct>,
    val fillingPortion: Double,
    val saladPortion: Double,
    val totalWeight: Double,
    val location: String,
    val inventoryValue: Double
)
