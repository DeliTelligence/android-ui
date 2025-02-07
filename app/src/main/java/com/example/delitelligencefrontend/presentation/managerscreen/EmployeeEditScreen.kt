package com.example.delitelligencefrontend.presentation.employeedetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.delitelligencefrontend.enumformodel.EmployeeTitle
import com.example.delitelligencefrontend.model.EmployeeCreate
import com.example.delitelligencefrontend.model.EmployeeUpdate
import com.example.delitelligencefrontend.presentation.viewmodel.ManageEmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeEditScreen(
    navController: NavController,
    employeeId: String?,
    viewModel: ManageEmployeeViewModel = hiltViewModel()
) {
    // Fetch the employee using the employeeId
    val employees by viewModel.employees.collectAsState()
    val employee = employees.find { it.id == employeeId }

    var firstName by remember(employee) { mutableStateOf(employee?.employeeFirstName ?: "") }
    var lastName by remember(employee) { mutableStateOf(employee?.employeeLastName ?: "") }
    var title by remember(employee) { mutableStateOf(employee?.employeeTitle ?: EmployeeTitle.EMPLOYEE) }
    var password by remember { mutableStateOf("") }

    var expandedTitle by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (employeeId == null) "Create Employee" else "Edit Employee")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Employee Title Dropdown using the same method as other screen
                ExposedDropdownMenuBox(
                    expanded = expandedTitle,
                    onExpandedChange = { expandedTitle = !expandedTitle }
                ) {
                    TextField(
                        value = title.name,
                        onValueChange = { },
                        label = { Text("Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTitle)
                        },
                        readOnly = true,
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTitle,
                        onDismissRequest = { expandedTitle = false }
                    ) {
                        EmployeeTitle.values().forEach { titleOption ->
                            DropdownMenuItem(
                                text = { Text(titleOption.name) },
                                onClick = {
                                    title = titleOption
                                    expandedTitle = false
                                }
                            )
                        }
                    }
                }

                // Show password field only if creating a new employee
                if (employeeId == null) {
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Button(
                    onClick = {
                        if (employeeId == null) {
                            // Handle create
                            viewModel.createEmployee(
                                EmployeeCreate(
                                    employeeFirstName = firstName,
                                    employeeLastName = lastName,
                                    employeeTitle = title,
                                    employeePassword = password
                                )
                            )
                        } else {
                            // Handle update
                            viewModel.updateEmployee(
                                EmployeeUpdate(
                                    employeeId = employeeId,
                                    employeeFirstName = firstName,
                                    employeeLastName = lastName,
                                    employeeTitle = title,
                                    employeePassword = password
                                )
                            )

                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (employeeId == null) "Create" else "Update")
                }
            }
        }
    )
}