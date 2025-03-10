package com.example.delitelligencefrontend.model

import com.example.delitelligencefrontend.enumformodel.EmployeeTitle

data class EmployeeFetch(
    val id: String,
    val employeeFirstName: String,
    val employeeLastName: String,
    val employeeLoggedIn: Boolean,
    val employeeTitle: EmployeeTitle,
    val employeeEmail: String,
    val hireDate: String,
    val employeePassword: String,
    val totalTransactions: Int,
    val totalWastePercentage: Double
)

