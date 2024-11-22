/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/


package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAllProductsQuery
import com.example.delitelligence.GetProductByIDQuery
import com.example.delitelligence.GetProductsByTypeQuery
import com.example.delitelligencefrontend.model.Product

fun GetProductByIDQuery.GetProductById.toProduct(): Product {
    return Product(
        productName = this.productName,
        standardWeight = this.standardWeight, // Adjust if available in response
        productPrice = this.productPrice,
        productImage = this.productImage,
        productType = this.productType

    )
}
fun GetProductsByTypeQuery.GetProductsByType.toProduct(): Product {
    return Product(
        productName = this.productName,
        standardWeight = this.standardWeight, // Adjust if available in response
        productPrice = this.productPrice,
        productImage = this.productImage,
        productType = this.productType

    )
}
fun GetAllProductsQuery.GetAllProduct.toProduct(): Product {
    return Product(
        productName = this.productName,
        standardWeight = this.standardWeight, // Adjust if available in response
        productPrice = this.productPrice,
        productImage = this.productImage,
        productType = this.productType
    )
}


