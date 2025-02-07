package com.example.delitelligencefrontend.model

import com.example.delitelligencefrontend.enumformodel.EmployeeTitle

data class EmployeeCreate(
    val employeeFirstName: String,
    val employeeLastName: String,
    val employeeTitle: EmployeeTitle,
    val employeePassword: String
)
