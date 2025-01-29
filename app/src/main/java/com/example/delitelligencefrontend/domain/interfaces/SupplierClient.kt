package com.example.delitelligencefrontend.domain.interfaces

import com.example.delitelligencefrontend.model.Supplier

interface SupplierClient {
    suspend fun getAllSuppliers(): List<Supplier>

}