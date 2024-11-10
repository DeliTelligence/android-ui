/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 10/10/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.domain

class GetEmployeesUseCase(
    private val employeeClient: EmployeeClient
) {

    suspend fun execute(): List<Employee> {
        return employeeClient
            .getEmployees()
            .sortedBy { it.employeeFirstName }
    }
}

