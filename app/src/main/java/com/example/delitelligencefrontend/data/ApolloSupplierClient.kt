/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.data

import com.apollographql.apollo.ApolloClient
import com.example.delitelligence.GetAllSuppliersQuery
import com.example.delitelligencefrontend.domain.interfaces.SupplierClient
import com.example.delitelligencefrontend.model.Supplier
import com.example.delitelligencefrontend.model.mapper.toSupplier

class ApolloSupplierClient(
    private val apolloClient: ApolloClient
): SupplierClient {
    override suspend fun getAllSuppliers(): List<Supplier> {
            return apolloClient
                .query(GetAllSuppliersQuery())
                .execute()
                .data
                ?.getAllSuppliers
                ?.mapNotNull{it?.toSupplier()}
                ?:emptyList()

    }
}