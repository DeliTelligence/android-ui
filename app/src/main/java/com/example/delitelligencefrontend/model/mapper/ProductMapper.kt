/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/


package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAllProductsQuery
import com.example.delitelligence.GetProductByIDQuery
import com.example.delitelligence.GetProductsByTypeQuery
import com.example.delitelligence.type.EmployeePosition
import com.example.delitelligencefrontend.enumformodel.EmployeeTitle
import com.example.delitelligencefrontend.enumformodel.ProductType
import com.example.delitelligencefrontend.enumformodel.StandardType
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
        id = this.id,
        productName = this.productName,
        standardWeightProducts = null, // Adjust if available in the response
        productPrice = this.productPrice,
        productImageDto = this.productImageDto,
        productDescription = this.productDescription,
        productType = this.productType?.toProductType()
    )
}

// Mapping extension function for GetProductsByTypeQuery.GetProductsByType
fun GetProductsByTypeQuery.GetProductsByType.toProduct(): Product {
    return Product(
        id = this.id,
        productName = this.productName,
        standardWeightProducts = null, // Adjust if available in the response
        productPrice = this.productPrice,
        productImageDto = this.productImageDto,
        productDescription = this.productDescription,
        productType = this.productType?.toProductType()
    )
}

// Mapping extension function for GetAllProductsQuery.GetAllProduct
fun GetAllProductsQuery.GetAllProduct.toProduct(): Product {
    return Product(
        id = this.id,
        productName = this.productName,
        standardWeightProducts = this.standardWeightProducts.safeMap { it.toStandardWeightProduct() },
        productPrice = this.productPrice?.toDouble(),
        productImageDto = this.productImageDto,
        productDescription = this.productDescription,
        productType = this.productType?.toProductType()
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
        standardWeightId = this.standardWeightId,
        standardType = this.standardType?.toStandardType()
    )
}

fun com.example.delitelligence.type.StandardType.toStandardType(): StandardType {
    return when (this) {
        com.example.delitelligence.type.StandardType.FILLING -> StandardType.FILLING
        com.example.delitelligence.type.StandardType.SALAD -> StandardType.SALAD
        com.example.delitelligence.type.StandardType.TO_GO -> StandardType.TO_GO
        com.example.delitelligence.type.StandardType.UNKNOWN__ -> StandardType.UNKNOWN
    }
}

fun com.example.delitelligence.type.ProductType.toProductType(): ProductType {
    return when (this) {
        com.example.delitelligence.type.ProductType.BREAD -> ProductType.BREAD
        com.example.delitelligence.type.ProductType.HOT_FOOD -> ProductType.HOT_FOOD
        com.example.delitelligence.type.ProductType.COLD_FOOD -> ProductType.COLD_FOOD
        com.example.delitelligence.type.ProductType.BREAKFAST_FOOD -> ProductType.BREAKFAST_FOOD
        com.example.delitelligence.type.ProductType.MADE_FOOD_HOT -> ProductType.MADE_FOOD_HOT
        com.example.delitelligence.type.ProductType.MAIN_FILLING_FOOD -> ProductType.MAIN_FILLING_FOOD
        com.example.delitelligence.type.ProductType.MADE_FOOD_COLD -> ProductType.MADE_FOOD_COLD
        com.example.delitelligence.type.ProductType.UNKNOWN__ -> ProductType.UNKNOWN

    }
}


