package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.EmployeeInputDto
import com.example.delitelligencefrontend.enumformodel.ProductType
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.Product

interface ProductClient {
    suspend fun getProductsById(productId: String): Product?
    suspend fun getProductsByType(productType: com.example.delitelligence.type.ProductType): List<Product>
    suspend fun getAllProducts(): List<Product>
}