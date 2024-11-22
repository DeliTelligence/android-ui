
/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/
package com.example.delitelligencefrontend.data

import com.apollographql.apollo.ApolloClient
import com.example.delitelligence.GetEmployeesQuery
import com.example.delitelligence.type.EmployeeInputDto
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.domain.EmployeeClient
import com.example.delitelligencefrontend.model.mapper.toEmployee

class ApolloEmployeeClient(
    private val apolloClient: ApolloClient
): EmployeeClient {
    override suspend fun createEmployee(inputDto: EmployeeInputDto): Employee {

        TODO("Not yet implemented")

    }

    override suspend fun getEmployees(): List<Employee> {
        return apolloClient
            .query(GetEmployeesQuery())
            .execute()
            .data
            ?.getEmployees
            ?.mapNotNull { it?.toEmployee() } // Use mapNotNull to filter out nulls
            ?: emptyList()
    }
}

