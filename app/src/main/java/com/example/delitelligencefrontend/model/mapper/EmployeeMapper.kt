/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetEmployeesQuery
import com.example.delitelligencefrontend.model.Employee

fun GetEmployeesQuery.GetEmployee.toEmployee(): Employee {
    return Employee(
        employeeFirstName = employeeFirstName ?: "No First Name",
        employeeLastName = employeeLastName ?: "No Last Name",
        hireDate = hireDate ?: "No Date"
    )
}