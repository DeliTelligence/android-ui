/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 10/10/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.EmployeeInputDto
import com.example.delitelligencefrontend.domain.interfaces.EmployeeClient
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.EmployeeFetch

class EmployeesUseCase(
    private val employeeClient: EmployeeClient
) {

    suspend fun execute(): List<EmployeeFetch> {
        return employeeClient
            .getEmployees()
            .sortedBy { it.employeeFirstName }
    }
    suspend fun execute(password: String): Employee? {
        return employeeClient
            .employeeLogIn(password)
    }
    suspend fun execute(input: EmployeeInputDto): String? {
        return employeeClient
            .createEmployee(input)
    }
}

