/* https://developer.android.com/develop/ui/compose/layouts/basics
Compose Layout Basics
Date 21/11/2024 accessed
All code here is adapted from the website*/

package com.example.delitelligencefrontend.presentation.mainmenu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.delitelligencefrontend.presentation.Screen

@Composable
fun StoreScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StoreButton(
            text = "Make a Product",
            onClick = { navController.navigate(Screen.MakeProductScreen.route) }        )

        Spacer(modifier = Modifier.height(16.dp))

        StoreButton(
            text = "Hot Food to Go",
            onClick = {navController.navigate(Screen.HotFoodToGoScreen.route)}
        )

        Spacer(modifier = Modifier.height(16.dp))

        StoreButton(
            text = "Make a Salad",
            onClick = { navController.navigate(Screen.MakeSaladScreen.route) }
        )
    }
}

@Composable
fun StoreButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(text)
    }
}