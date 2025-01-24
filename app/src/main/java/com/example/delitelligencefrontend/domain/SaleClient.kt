package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.DeliSaleInputDto
import com.example.delitelligencefrontend.model.Product

interface SaleClient {
    suspend fun createSale(input: DeliSaleInputDto): String?

}