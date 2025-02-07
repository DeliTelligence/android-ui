
/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/
package com.example.delitelligencefrontend.data

import com.apollographql.apollo.ApolloClient
import com.example.delitelligence.CreateEmployeeMutation
import com.example.delitelligence.EmployeeLogInQuery
import com.example.delitelligence.GetAllProductsQuery
import com.example.delitelligence.GetEmployeesQuery
import com.example.delitelligence.UpdateEmployeeMutation
import com.example.delitelligence.type.EmployeeInputCreateDto
import com.example.delitelligence.type.EmployeeInputUpdateDto
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.domain.interfaces.EmployeeClient
import com.example.delitelligencefrontend.model.EmployeeFetch
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.mapper.toEmployee
import com.example.delitelligencefrontend.model.mapper.toProduct

class ApolloEmployeeClient(
    private val apolloClient: ApolloClient
): EmployeeClient {
    override suspend fun createEmployee(input: EmployeeInputCreateDto): String? {
        return try {
            val response = apolloClient
                .mutation(CreateEmployeeMutation(input))
                .execute()

            // Assuming response.data?.createSale returns a string
            response.data?.createEmployee
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun updateEmployee(input: EmployeeInputUpdateDto): String? {
        return try {
            val response = apolloClient
                .mutation(UpdateEmployeeMutation(input))
                .execute()

            // Assuming response.data?.createSale returns a string
            response.data?.editEmployee
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getEmployees(): List<EmployeeFetch> {
        return apolloClient
            .query(GetEmployeesQuery())
            .execute()
            .data
            ?.getAllEmployees
            ?.mapNotNull{it?.toEmployee()}
            ?:emptyList()
    }

    override suspend fun employeeLogIn(password: String): Employee? {
        return apolloClient
            .query(EmployeeLogInQuery(password))
            .execute()
            .data
            ?.employeeLogin
            ?.toEmployee()    }



}

