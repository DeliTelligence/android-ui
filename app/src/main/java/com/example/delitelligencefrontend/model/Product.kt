package com.example.delitelligencefrontend.model

import com.example.delitelligencefrontend.enumformodel.ProductType
import java.util.UUID

data class Product(
    val id: String?,
    val productName: String?,
    val standardWeightProducts: List<StandardWeightProduct>?,
    val productPrice: Double?,
    val productImageDto: String?,
    val productType: ProductType?,
    val productDescription: String?,
    )