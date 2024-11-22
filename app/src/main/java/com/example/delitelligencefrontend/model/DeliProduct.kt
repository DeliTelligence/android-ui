package com.example.delitelligencefrontend.model

data class DeliProduct(
    val deliProductId: String,
    val products: List<Product>,
    val combinedWeight: Float,
    val deliProductPrice: Float,
    val deliProductQuantity: Int,
    val weightToPrice: Boolean
)
