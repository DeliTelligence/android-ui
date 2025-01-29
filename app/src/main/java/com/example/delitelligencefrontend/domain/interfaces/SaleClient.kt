package com.example.delitelligencefrontend.domain.interfaces

import com.example.delitelligence.type.DeliSaleInputDto

interface SaleClient {
    suspend fun createSale(input: DeliSaleInputDto): String?

}