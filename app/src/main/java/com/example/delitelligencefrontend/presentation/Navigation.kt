/*https://chatgpt.com
  prompt 'How to build a navigation for my screens in android using jetpack compose'  */

package com.example.delitelligencefrontend.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(
    onBluetoothStateChanged: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.StartScreen.route) {
        composable(Screen.StartScreen.route) {
            StartScreen(navController = navController)
        }

        composable(Screen.FoodScalesScreen.route) {
            FoodScalesScreen()
        }
    }
}

sealed class Screen(val route: String) {
    object StartScreen : Screen("start_screen")
    object FoodScalesScreen : Screen("food_scales_screen")
}