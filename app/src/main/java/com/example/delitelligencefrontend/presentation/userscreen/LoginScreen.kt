package com.example.delitelligencefrontend.presentation.userscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.delitelligencefrontend.R
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.presentation.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Make the Box bigger to increase the image size
        Box(
            modifier = Modifier
                .size(300.dp) // Increased size to 300dp, adjust as needed
                .background(MaterialTheme.colorScheme.background) // Adjusts the background, can be `Color.White` if preferred
                .padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.delitelligencelogo),
                contentDescription = "DeliTelligence Logo",
                modifier = Modifier.fillMaxSize() // Ensures the image fills the entire size of the Box
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                viewModel.login(
                    password = password,
                    onSuccess = { employee ->
                        isLoading = false
                        navController.navigate("store_screen") {
                            popUpTo("login_screen") { inclusive = true }
                        }
                    },
                    onError = { error ->
                        isLoading = false
                        errorMessage = error
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}