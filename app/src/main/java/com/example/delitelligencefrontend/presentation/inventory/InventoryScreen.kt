/* https://www.youtube.com/watch?v=6_wK_Ud8--0&ab_channel=PhilippLackner
The Jetpack Compose Beginner Crash Course for 2023 💻 (Android Studio Tutorial)
Date 6/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.presentation.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.delitelligencefrontend.presentation.Screen
import com.example.delitelligencefrontend.presentation.mainmenu.StoreButton

@Composable
fun InventoryScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StoreButton(
            text = "Add Delivery",
            onClick = { navController.navigate(Screen.AddToInventoryScreen.route) }
        )


        Spacer(modifier = Modifier.height(16.dp))

        StoreButton(
            text = "Check Inventory",
            onClick = { navController.navigate(Screen.CheckInventoryScreen.route) }
        )
    }
}