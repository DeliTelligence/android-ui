package com.example.delitelligencefrontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.domain.EmployeesUseCase
import com.example.delitelligencefrontend.domain.interfaces.EmployeeClient
import com.example.delitelligencefrontend.model.EmployeeCreate
import com.example.delitelligencefrontend.model.EmployeeUpdate
import com.example.delitelligencefrontend.model.mapper.DeliSaleMapper
import com.example.delitelligencefrontend.model.mapper.PostEmployeeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageEmployeeViewModel @Inject constructor(
    private val employeeUseCase: EmployeesUseCase
) : ViewModel() {

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