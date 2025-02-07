package com.example.delitelligencefrontend.domain.interfaces


import com.example.delitelligence.type.ProductCreateDto
import com.example.delitelligence.type.ProductUpdateDto
import com.example.delitelligencefrontend.model.Product

interface ProductClient {
    suspend fun createProduct(input: ProductCreateDto): String?
    suspend fun updateProduct(input: ProductUpdateDto): String?
    suspend fun getProductsById(productId: String): Product?
    suspend fun getProductsByType(productType: com.example.delitelligence.type.ProductType): List<Product>
    suspend fun getAllProducts(): List<Product>
}