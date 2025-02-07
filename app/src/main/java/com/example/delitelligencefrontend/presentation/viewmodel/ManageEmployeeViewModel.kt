package com.example.delitelligencefrontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.domain.EmployeesUseCase
import com.example.delitelligencefrontend.domain.interfaces.EmployeeClient
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.EmployeeCreate
import com.example.delitelligencefrontend.model.EmployeeFetch
import com.example.delitelligencefrontend.model.EmployeeUpdate
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.mapper.DeliSaleMapper
import com.example.delitelligencefrontend.model.mapper.PostEmployeeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageEmployeeViewModel @Inject constructor(
    private val employeeUseCase: EmployeesUseCase
) : ViewModel() {

    private val _employees = MutableStateFlow<List<EmployeeFetch>>(emptyList())
    val employees: StateFlow<List<EmployeeFetch>> = _employees

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        fetchAllEmployees()
    }

    fun fetchAllEmployees() {
        viewModelScope.launch {
            _employees.value = employeeUseCase.execute()
        }
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _employees.value = if (query.isEmpty()) {
                employeeUseCase.execute()
            } else {
                employeeUseCase.execute().filter {
                    it.employeeFirstName.contains(query, ignoreCase = true) ?: false
                }
            }
        }
    }

    fun createEmployee(employeeCreate: EmployeeCreate) {
        viewModelScope.launch {
            try {
                val inputDto = PostEmployeeMapper.INSTANCE.toEmployeeInputCreateDto(employeeCreate)
                val response = employeeUseCase.execute(inputDto)
                // Handle response (e.g., update UI, show a success message, etc.)
                println("Create Employee Response: $response")
            } catch (e: Exception) {
                // Handle error (e.g., show an error message)
                println("Error Creating Employee: ${e.message}")
            }
        }
    }

    fun updateEmployee(employeeUpdate: EmployeeUpdate) {
        viewModelScope.launch {
            try {
                val inputDto = PostEmployeeMapper.INSTANCE.toEmployeeInputUpdateDto(employeeUpdate)
                val response = inputDto?.let { employeeUseCase.execute(it) }
                // Handle response (e.g., update UI, show a success message, etc.)
                println("Update Employee Response: $response")
            } catch (e: Exception) {
                // Handle error (e.g., show an error message)
                println("Error Updating Employee: ${e.message}")
            }
        }
    }
}