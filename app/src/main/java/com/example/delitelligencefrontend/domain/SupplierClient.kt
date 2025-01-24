package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.DeliSaleInputDto
import com.example.delitelligencefrontend.model.Supplier

interface SupplierClient {
    suspend fun getAllSuppliers(): List<Supplier>

}