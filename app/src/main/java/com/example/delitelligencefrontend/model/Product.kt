package com.example.delitelligencefrontend.model

import com.example.delitelligencefrontend.enumformodel.ProductType

data class Product(
    val productName: String?,
    val standardWeight: Double?,
    val productPrice: Double?,
    val productImage: String?,
    val productType: String?
)