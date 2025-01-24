/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 10/10/2024 accessed
All code here is adapted from the video*/
package com.example.delitelligencefrontend.domain

import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.Supplier

class GetSupplierUseCase(
    private val supplierClient: SupplierClient
) {
    suspend fun execute(): List<Supplier> {
        return supplierClient.getAllSuppliers()
    }
}