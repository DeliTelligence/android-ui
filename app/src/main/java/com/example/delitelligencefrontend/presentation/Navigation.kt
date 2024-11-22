/*https://chatgpt.com
  prompt 'How to build a navigation for my screens in android using jetpack compose'  */

package com.example.delitelligencefrontend.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.delitelligencefrontend.presentation.mainmenu.HotFoodToGoScreen
import com.example.delitelligencefrontend.presentation.mainmenu.MakeProductScreen
import com.example.delitelligencefrontend.presentation.mainmenu.MakeSaladScreen
import com.example.delitelligencefrontend.presentation.mainmenu.StoreScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.StartScreen.route) {
        composable(Screen.StartScreen.route) {
            StartScreen(navController = navController)
        }

        composable(Screen.StoreScreen.route) {
            BaseScreen(navController) {
                StoreScreen(navController)
            }
        }
        composable(Screen.MakeProductScreen.route) {
            BaseScreen(navController) {
                MakeProductScreen()
            }
        }


        composable(Screen.InventoryScreen.route) {
            BaseScreen(navController) {
                // Replace with your InventoryScreen when it's implemented
                Text("Inventory Screen")
            }
        }
        composable(Screen.HotFoodToGoScreen.route) {
            BaseScreen(navController) {
                // Replace with your InventoryScreen when it's implemented
                HotFoodToGoScreen()
            }
        }
        composable(Screen.MakeSaladScreen.route) {
            BaseScreen(navController) {
                // Replace with your InventoryScreen when it's implemented
                MakeSaladScreen()
            }
        }


        // Add other screens as needed
    }
}

sealed class Screen(val route: String) {
    object StartScreen : Screen("start_screen")
    object FoodScalesScreen : Screen("food_scales_screen")
    object StoreScreen : Screen("store_screen")
    object MakeProductScreen : Screen("make_product_screen")
    object LoginScreen : Screen("login_screen")
    object InventoryScreen : Screen("inventory_screen")
    object HotFoodToGoScreen : Screen("hot_food_to_go_screen")
    object MakeSaladScreen : Screen("make_salad_screen")

}

