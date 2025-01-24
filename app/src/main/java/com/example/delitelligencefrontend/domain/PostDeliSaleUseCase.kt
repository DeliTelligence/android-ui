/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 10/10/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.DeliSaleInputDto
import com.example.delitelligencefrontend.model.Product

class PostDeliSaleUseCase(
    private val saleClient: SaleClient
)  {
    suspend fun execute(input: DeliSaleInputDto): String? {
        return saleClient.createSale(input)
    }
}