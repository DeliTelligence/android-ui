package com.example.delitelligencefrontend.model

import com.example.delitelligencefrontend.enumformodel.EmployeeTitle

data class EmployeeUpdate(
    val employeeId: String,
    val employeeFirstName: String,
    val employeeLastName: String,
    val employeeTitle: EmployeeTitle,
    val employeePassword: String
)
