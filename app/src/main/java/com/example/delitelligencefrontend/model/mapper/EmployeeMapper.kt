/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.EmployeeLogInQuery
import com.example.delitelligence.GetEmployeesQuery
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.EmployeeFetch
import java.util.UUID

fun GetEmployeesQuery.GetEmployee.toEmployee(): EmployeeFetch {
    return EmployeeFetch(
        employeeFirstName = employeeFirstName ?: "No First Name",
        employeeLastName = employeeLastName ?: "No Last Name",
        hireDate = hireDate ?: "No Date",
        employeeTitle = employeeTitle ?: "No Title",
        employeeLoggedIn = employeeLoggedIn ?: false
    )
}

fun EmployeeLogInQuery.EmployeeLogin.toEmployee(): Employee {
    return Employee(
        employeeId = id ?: "None",
        employeeFirstName = employeeFirstName ?: "No First Name",
        employeeLastName = employeeLastName ?: "No Last Name",
        hireDate = hireDate ?: "No Date",
        employeeTitle = (employeeTitle ?: "No Title").toString(),
        employeeLoggedIn = employeeLoggedIn ?: false
    )
}