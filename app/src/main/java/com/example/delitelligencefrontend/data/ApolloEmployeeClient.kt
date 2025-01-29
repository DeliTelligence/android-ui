
/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/
package com.example.delitelligencefrontend.data

import com.apollographql.apollo.ApolloClient
import com.example.delitelligence.CreateEmployeeMutation
import com.example.delitelligence.EmployeeLogInQuery
import com.example.delitelligence.GetEmployeesQuery
import com.example.delitelligence.type.EmployeeInputDto
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.domain.interfaces.EmployeeClient
import com.example.delitelligencefrontend.model.EmployeeFetch
import com.example.delitelligencefrontend.model.mapper.toEmployee

class ApolloEmployeeClient(
    private val apolloClient: ApolloClient
): EmployeeClient {
    override suspend fun createEmployee(input: EmployeeInputDto): String? {
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

    override suspend fun getEmployees(): List<EmployeeFetch> {
        return apolloClient
            .query(GetEmployeesQuery())
            .execute()
            .data
            ?.getEmployees
            ?.mapNotNull { it?.toEmployee() } // Use mapNotNull to filter out nulls
            ?: emptyList()
    }

    override suspend fun employeeLogIn(password: String): Employee? {
        return apolloClient
            .query(EmployeeLogInQuery(password))
            .execute()
            .data
            ?.employeeLogin
            ?.toEmployee()    }



}

