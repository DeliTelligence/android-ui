/* https://www.youtube.com/watch?v=6_wK_Ud8--0&ab_channel=PhilippLackner
The Jetpack Compose Beginner Crash Course for 2023 💻 (Android Studio Tutorial)
Date 6/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delitelligencefrontend.domain.Employee
import com.example.delitelligencefrontend.presentation.viewmodel.EmployeeViewModel

@Composable
fun EmployeeScreen(
    state: EmployeeViewModel.EmployeeState,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.employees) { employee ->
                    EmployeeItem(
                        employee = employee,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { /* Handle click event if needed */ }
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray) // Background color for items
                            .padding(16.dp) // Inner padding for the item
                    )
                }
            }
        }
    }
}

@Composable
private fun EmployeeItem(
    employee: Employee,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        // You can customize this to display an avatar or photo if available
        // Image(painter = painterResource(id = R.drawable.ic_employee), contentDescription = null)

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${employee.employeeFirstName} ${employee.employeeLastName}",
                fontSize = 20.sp,
                color = Color.Black // Change text color if needed
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Hired on: ${employee.hireDate}",
                fontSize = 16.sp,
                color = Color.Gray // Subtle color for less important text
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Position: ${employee.employeeFirstName}", // Assuming you have a position field
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}
