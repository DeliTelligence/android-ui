/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/


package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAllProductsQuery
import com.example.delitelligence.GetProductByIDQuery
import com.example.delitelligence.GetProductsByTypeQuery
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.StandardWeight
import com.example.delitelligencefrontend.model.StandardWeightProduct

// Utility function to safely map a nullable list of responses to models
inline fun <T, R> List<T?>?.safeMap(mapper: (T) -> R): List<R> {
    return this?.mapNotNull { it?.let(mapper) } ?: emptyList()
}

// Mapping extension function for GetProductByIDQuery.GetProductById
fun GetProductByIDQuery.GetProductById.toProduct(): Product {
    return Product(
        productId = this.id,
        productName = this.productName,
        standardWeightProducts = null, // Adjust if available in the response
        productPrice = this.productPrice,
        productImageDto = this.productImageDto,
        productType = this.productType
    )
}

// Mapping extension function for GetProductsByTypeQuery.GetProductsByType
fun GetProductsByTypeQuery.GetProductsByType.toProduct(): Product {
    return Product(
        productId = this.id,
        productName = this.productName,
        standardWeightProducts = null, // Adjust if available in the response
        productPrice = this.productPrice,
        productImageDto = this.productImageDto,
        productType = this.productType
    )
}

// Mapping extension function for GetAllProductsQuery.GetAllProduct
fun GetAllProductsQuery.GetAllProduct.toProduct(): Product {
    return Product(
        productId = this.id,
        productName = this.productName,
        standardWeightProducts = this.standardWeightProducts.safeMap { it.toStandardWeightProduct() },
        productPrice = this.productPrice?.toDouble(),
        productImageDto = this.productImageDto,
        productType = this.productType
    )
}

// Extension function to map the StandardWeightProduct response to model
fun GetAllProductsQuery.StandardWeightProduct.toStandardWeightProduct(): StandardWeightProduct {
    return StandardWeightProduct(
        standardWeight = this.standardWeight?.toStandardWeight(),
        standardWeightValue = this.standardWeightValue?.toFloat()
    )
}

// Extension function to map the StandardWeight response to model
fun GetAllProductsQuery.StandardWeight.toStandardWeight(): StandardWeight {
    return StandardWeight(
        standardType = this.standardType
    )
}



