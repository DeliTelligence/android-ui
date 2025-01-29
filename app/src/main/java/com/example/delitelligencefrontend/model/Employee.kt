package com.example.delitelligencefrontend.model

import java.util.UUID


data class Employee(
    val employeeId: String,
    val employeeFirstName: String,
    val employeeLastName: String,
    val employeeLoggedIn: Boolean,
    val hireDate: String,
    val employeeTitle: String
)
