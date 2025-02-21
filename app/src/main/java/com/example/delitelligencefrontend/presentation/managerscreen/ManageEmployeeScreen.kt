/*https://chatgpt.com
prompt: 'build a screen that will display the employees data from the graphql request and display it in rows
and add, edit and delete buttons mapped by the viewModel.
*/

package com.example.delitelligencefrontend.presentation.managerscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.delitelligencefrontend.model.EmployeeFetch
import com.example.delitelligencefrontend.presentation.viewmodel.ManageEmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEmployeeScreen(
    viewModel: ManageEmployeeViewModel = hiltViewModel(),
    navController: NavController
) {
    val employees by viewModel.employees.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllEmployees()
    }

    // Refresh products when returning from create or edit screens
    LaunchedEffect(navController.currentBackStackEntry) {
        navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("refresh")?.let {
            if (it) {
                viewModel.fetchAllEmployees()
                navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("refresh")
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Manage Employee Screen") })
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Left Column for Search Bar and Add Button
            Column(
                modifier = Modifier
                    .width(250.dp)
                    .padding(end = 16.dp)
            ) {
                SearchBar(
                    searchQuery = searchQuery,
                    onQueryChange = { query -> viewModel.searchEmployees(query) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Add Employee Button
                Button(
                    onClick = {
                        navController.navigate("create_employee")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Employee")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Employee")
                }
            }

            // Right Column for Employee Display
            Column(
                modifier = Modifier.weight(1f).padding(16.dp)
            ) {
                EmployeeDisplay(
                    employees = employees,
                    onEdit = { employee ->
                        navController.navigate("edit_employee/${employee.id}")
                    },
                    onDelete = { viewModel.deleteEmployee(it.id)
                        viewModel.fetchAllEmployees()},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun EmployeeDisplay(
    employees: List<EmployeeFetch>,
    onEdit: (EmployeeFetch) -> Unit,
    onDelete: (EmployeeFetch) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Use itemsIndexed to get the index of each item
        itemsIndexed(employees) { index, employee ->
            Column {
                EmployeeRow(
                    employee = employee,
                    onEdit = onEdit,
                    onDelete = onDelete,
                    modifier = Modifier.fillMaxWidth() // adapt to available width
                )
                if (index < employees.size - 1) {
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth() // adapt to available width
                    )
                }
            }
        }
    }
}

@Composable
fun EmployeeRow(
    employee: EmployeeFetch,
    onEdit: (EmployeeFetch) -> Unit,
    onDelete: (EmployeeFetch) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = employee.employeeFirstName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = employee.employeeLastName,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = employee.hireDate,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Calculate waste per transaction percentage and determine color and message
            val wastePerTransaction = if (employee.totalTransactions > 0) {
                employee.totalWastePercentage / employee.totalTransactions
            } else {
                0.0
            }

            val (wasteColor, wasteMessage) = when {
                wastePerTransaction <= -10 -> Color.Red to "Underfilling on average by %.2f%%".format(-wastePerTransaction)
                wastePerTransaction in -9.99..-6.0 -> Color.Yellow to "Underfilling on average by %.2f%%".format(-wastePerTransaction)
                wastePerTransaction < 0 -> Color.Green to "Underfilling by %.2f%%".format(-wastePerTransaction)
                wastePerTransaction <= 10 -> Color.Green to "Overfilling by %.2f%%".format(wastePerTransaction)
                wastePerTransaction in 6.0..9.99 -> Color.Yellow to "Overfilling by %.2f%%".format(wastePerTransaction)
                else -> Color.Red to "Overfilling by %.2f%%".format(wastePerTransaction)
            }

            Text(
                text = wasteMessage,
                color = wasteColor,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (employee.employeeLoggedIn) "Logged In" else "Not Logged In",
                color = if (employee.employeeLoggedIn) Color.Green else Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Action Buttons
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = { onEdit(employee) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .width(80.dp)
                    .height(36.dp)
            ) {
                Text("Edit", maxLines = 1)
            }
            Button(
                onClick = { onDelete(employee) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(80.dp)
                    .height(36.dp)
            ) {
                Text("Delete", maxLines = 1)
            }
        }
    }
}