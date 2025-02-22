/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video*/
package com.example.delitelligencefrontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delitelligencefrontend.domain.EmployeesUseCase
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val employeesUseCase: EmployeesUseCase,
    private val session: Session
) : ViewModel() {

    fun login(password: String, onSuccess: (Employee) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val employee = employeesUseCase.execute(password)
                if (employee != null) {
                    session.setUser(employee)
                    onSuccess(employee)
                } else {
                    onError("Invalid credentials. Please try again.")
                }
            } catch (e: Exception) {
                onError("An error occurred: ${e.message}")
            }
        }
    }
}