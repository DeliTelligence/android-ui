
/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.domain.EmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val getEmployeesUseCase: EmployeesUseCase

): ViewModel() {

    private val _state = MutableStateFlow(EmployeeState())
    val state = _state.asStateFlow()

//    init {
//        viewModelScope.launch {
//            _state.update { it.copy(
//                isLoading = true
//            ) }
//            _state.update { it.copy(
//                employees = getEmployeesUseCase.execute(),
//                isLoading = false
//            ) }
//        }
//    }

//    fun selectCountry(code: String) {
//        viewModelScope.launch {
//            _state.update { it.copy(
//                selectedCountry = getCountryUseCase.execute(code)
//            ) }
//        }
//    }

//    fun dismissCountryDialog() {
//        _state.update { it.copy(
//            selectedCountry = null
//        ) }
//    }

    data class EmployeeState(
        val employees: List<Employee> = emptyList(),
        val isLoading: Boolean = false,
//        val selectedCountry: DetailedCountry? = null
    )
}