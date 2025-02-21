
/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.data

import com.apollographql.apollo.ApolloClient
import com.example.delitelligence.CreateProductMutation
import com.example.delitelligence.DeleteProductMutation
import com.example.delitelligence.EditProductMutation
import com.example.delitelligence.GetAllProductsQuery
import com.example.delitelligence.GetAllStandardWeightsQuery
/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

import com.example.delitelligence.GetProductByIDQuery
import com.example.delitelligence.GetProductByNameQuery
import com.example.delitelligence.GetProductsByTypeQuery
import com.example.delitelligence.UpdateEmployeeMutation
import com.example.delitelligence.type.EmployeeInputUpdateDto
import com.example.delitelligence.type.ProductCreateDto
import com.example.delitelligence.type.ProductUpdateDto

import com.example.delitelligencefrontend.domain.interfaces.ProductClient
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.StandardWeight
import com.example.delitelligencefrontend.model.mapper.toProduct
import com.example.delitelligencefrontend.model.mapper.toStandardWeight

class ApolloProductClient(
    private val apolloClient: ApolloClient

): ProductClient {
    override suspend fun createProduct(input: ProductCreateDto): String? {
        return try {
            val response = apolloClient
                .mutation(CreateProductMutation(input))
                .execute()

            // Assuming response.data?.createSale returns a string
            response.data?.createProduct
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun updateProduct(input: ProductUpdateDto): String? {
        return try {
            val response = apolloClient
                .mutation(EditProductMutation(input))
                .execute()

            // Assuming response.data?.createSale returns a string
            response.data?.editProduct
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun deleteProduct(id: String): String? {
        return try {
            val response = apolloClient
                .mutation(DeleteProductMutation(id))
                .execute()

            // Assuming response.data?.createSale returns a string
            response.data?.deleteProduct
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }    }

    override suspend fun getProductsById(productId: String): Product? {
       return apolloClient
           .query(GetProductByIDQuery(productId))
           .execute()
           .data
           ?.getProductById
           ?.toProduct()
    }

    override suspend fun getProductByName(productName: String): Product? {
        return apolloClient
            .query(GetProductByNameQuery(productName))
            .execute()
            .data
            ?.getProductByName
            ?.toProduct()    }

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

    override suspend fun getAllStandardWeights(): List<StandardWeight> {
        return apolloClient
            .query(GetAllStandardWeightsQuery())
            .execute()
            .data
            ?.getAllStandardWeights
            ?.mapNotNull{it?.toStandardWeight()}
            ?:emptyList()    }
}

