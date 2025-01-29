package com.example.delitelligencefrontend.domain.interfaces

import com.example.delitelligencefrontend.model.Product

interface ProductClient {
    suspend fun getProductsById(productId: String): Product?
    suspend fun getProductsByType(productType: com.example.delitelligence.type.ProductType): List<Product>
    suspend fun getAllProducts(): List<Product>
}