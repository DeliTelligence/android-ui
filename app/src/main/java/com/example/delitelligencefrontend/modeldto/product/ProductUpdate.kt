package com.example.delitelligencefrontend.modeldto.product

import com.example.delitelligencefrontend.enumformodel.ProductType


data class ProductUpdate(
    val id: String,
    val productName: String,
    val standardWeightProducts: List<StandardWeightProductUpdate>,
    val productPrice: Double,
    val productImageDto: String,
    val productDescription: String,
    val productType: ProductType
)
