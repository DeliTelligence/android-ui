
/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.data

import com.apollographql.apollo.ApolloClient
import com.example.delitelligence.GetAllProductsQuery
/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

import com.example.delitelligence.GetProductByIDQuery
import com.example.delitelligence.GetProductsByTypeQuery

import com.example.delitelligencefrontend.domain.interfaces.ProductClient
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.mapper.toProduct

class ApolloProductClient(
    private val apolloClient: ApolloClient

): ProductClient {
    override suspend fun getProductsById(productId: String): Product? {
       return apolloClient
           .query(GetProductByIDQuery(productId))
           .execute()
           .data
           ?.getProductById
           ?.toProduct()
    }

    override suspend fun getProductsByType(productType: com.example.delitelligence.type.ProductType): List<Product> {
        return apolloClient
            .query(GetProductsByTypeQuery(productType))
            .execute()
            .data
            ?.getProductsByType
            ?.mapNotNull{it?.toProduct()}
            ?:emptyList()
    }

    override suspend fun getAllProducts(): List<Product> {
        return apolloClient
            .query(GetAllProductsQuery())
            .execute()
            .data
            ?.getAllProducts
            ?.mapNotNull{it?.toProduct()}
            ?:emptyList()
    }
}

