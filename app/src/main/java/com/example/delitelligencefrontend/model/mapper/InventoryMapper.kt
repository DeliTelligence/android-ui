package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAllProductsQuery
import com.example.delitelligence.GetInventoryQuery
import com.example.delitelligencefrontend.model.Inventory
import com.example.delitelligencefrontend.model.InventoryProduct
import com.example.delitelligencefrontend.model.Product

fun GetInventoryQuery.GetInventory.toInventory(): Inventory {
    return Inventory(
        products = this.products?.map { it!!.toInventoryProduct() } ?: emptyList(),
        fillingPortion = this.fillingPortion ?: 0.0,
        saladPortion = this.saladPortion ?: 0.0,
        totalWeight = this.totalWeight ?: 0.0,
        location = this.location ?: "Unknown location",
        inventoryValue = this.inventoryValue ?: 0.0
    )
}

fun GetInventoryQuery.Product.toInventoryProduct(): InventoryProduct {
    return InventoryProduct(
        productName = this.productName ?: "Unknown product",
        productImage = this.productImageDto ?: ""
    )
}