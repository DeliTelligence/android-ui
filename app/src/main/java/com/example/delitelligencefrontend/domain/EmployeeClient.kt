package com.example.delitelligencefrontend.domain

import com.example.delitelligence.type.EmployeeInputDto
import com.example.delitelligencefrontend.model.Employee

interface EmployeeClient {
    suspend fun createEmployee(inputDto: EmployeeInputDto): Employee
    suspend fun getEmployees(): List<Employee>
}