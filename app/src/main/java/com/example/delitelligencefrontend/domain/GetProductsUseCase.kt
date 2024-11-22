/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 10/10/2024 accessed
All code here is adapted from the video*/


package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.ProductType
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.Product

class GetProductsUseCase(
    private val productClient: ProductClient

) {
    suspend fun execute(productId: String): Product? {
        return productClient.getProductsById(productId)
    }
    suspend fun execute(productType: ProductType): List<Product> {
        return productClient.getProductsByType(productType)
    }

    suspend fun execute(): List<Product> {
        return productClient.getAllProducts()
    }
}