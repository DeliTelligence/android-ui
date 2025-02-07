package com.example.delitelligencefrontend.modeldto.product

import com.example.delitelligencefrontend.enumformodel.ProductType


data class ProductCreate(
    val productName: String,
    val standardWeightProducts: List<StandardWeightProductCreate>,
    val productPrice: Double,
    val productImageDto: String,
    val productDescription: String,
    val productType: ProductType
)
