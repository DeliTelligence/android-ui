/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 10/10/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.EmployeeInputCreateDto
import com.example.delitelligence.type.EmployeeInputUpdateDto
import com.example.delitelligencefrontend.domain.interfaces.EmployeeClient
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.EmployeeFetch

class EmployeesUseCase(
    private val employeeClient: EmployeeClient
) {


    suspend fun execute(password: String): Employee? {
        return employeeClient
            .employeeLogIn(password)
    }

    suspend fun execute(): List<EmployeeFetch> {
        return employeeClient
            .getEmployees()
    }
    suspend fun execute(input: EmployeeInputCreateDto): String? {
        return employeeClient
            .createEmployee(input)
    }
    suspend fun execute(input: EmployeeInputUpdateDto): String? {
        return employeeClient
            .updateEmployee(input)
    }

    suspend fun executeDelete(id: String): String? {
        return employeeClient.deleteEmployee(id)
    }

}

