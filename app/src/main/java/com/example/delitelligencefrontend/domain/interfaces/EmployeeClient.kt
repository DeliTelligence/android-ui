package com.example.delitelligencefrontend.domain.interfaces

import com.example.delitelligence.type.EmployeeInputCreateDto
import com.example.delitelligence.type.EmployeeInputUpdateDto
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.EmployeeFetch

interface EmployeeClient {
    suspend fun createEmployee(input: EmployeeInputCreateDto): String?
    suspend fun updateEmployee(input: EmployeeInputUpdateDto): String?
    suspend fun deleteEmployee(id: String): String?
    suspend fun getEmployees(): List<EmployeeFetch>
    suspend fun employeeLogIn(password: String): Employee?
}