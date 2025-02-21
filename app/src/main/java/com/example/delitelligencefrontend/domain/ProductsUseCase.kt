/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 10/10/2024 accessed
All code here is adapted from the video*/


package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.ProductCreateDto
import com.example.delitelligence.type.ProductType
import com.example.delitelligence.type.ProductUpdateDto
import com.example.delitelligencefrontend.domain.interfaces.ProductClient
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.StandardWeight

class ProductsUseCase(
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
    suspend fun execute(input: ProductCreateDto): String?{
        return productClient.createProduct(input)
    }

    suspend fun execute(input: ProductUpdateDto): String?{
        return productClient.updateProduct(input)
    }

    suspend fun executeDelete(id: String): String? {
        return productClient.deleteProduct(id)
    }

    suspend fun executeGetStandardWeights(): List<StandardWeight> {
        return productClient.getAllStandardWeights()
    }
    suspend fun executeProductByName(productName: String): Product? {
        return productClient.getProductByName(productName)
    }
}