package com.example.delitelligencefrontend.domain.interfaces

import com.example.delitelligence.type.EmployeeInputDto
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.EmployeeFetch

interface EmployeeClient {
    suspend fun createEmployee(inputDto: EmployeeInputDto): String?
    suspend fun getEmployees(): List<EmployeeFetch>
    suspend fun employeeLogIn(password: String): Employee?
}