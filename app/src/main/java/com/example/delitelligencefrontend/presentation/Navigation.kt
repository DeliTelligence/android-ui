/*https://chatgpt.com
  prompt 'How to build a navigation for my screens in android using jetpack compose'  */

package com.example.delitelligencefrontend.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.delitelligencefrontend.presentation.employeedetails.EmployeeEditScreen
import com.example.delitelligencefrontend.presentation.inventory.AddToInventoryScreen
import com.example.delitelligencefrontend.presentation.inventory.CheckInventoryScreen
import com.example.delitelligencefrontend.presentation.inventory.InventoryScreen
import com.example.delitelligencefrontend.presentation.mainmenu.*
import com.example.delitelligencefrontend.presentation.managerscreen.ManageEmployeeScreen
import com.example.delitelligencefrontend.presentation.managerscreen.ManageProductScreen
import com.example.delitelligencefrontend.presentation.managerscreen.ManageUserScreen
import com.example.delitelligencefrontend.presentation.managerscreen.ManagerScreen
import com.example.delitelligencefrontend.presentation.managerscreen.ProductEditScreen
import com.example.delitelligencefrontend.presentation.userscreen.LoginScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
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
                InventoryScreen(navController)
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
        composable(Screen.AddToInventoryScreen.route) {
            BaseScreen(navController) {
                // Replace with your InventoryScreen when it's implemented
                AddToInventoryScreen()
            }
        }

        composable(Screen.CheckInventoryScreen.route) {
            BaseScreen(navController) {
                // Replace with your InventoryScreen when it's implemented
                CheckInventoryScreen()
            }
        }
        composable(Screen.ManagerScreen.route) {
            BaseScreen(navController) {
                // Replace with your InventoryScreen when it's implemented
                ManagerScreen(navController)
            }
        }
        composable(Screen.ManageProductScreen.route) {
            BaseScreen(navController) {
                // Replace with your InventoryScreen when it's implemented
                ManageProductScreen(navController = navController)
            }
        }
        composable("create_product") {
            BaseScreen(navController) {
                ProductEditScreen(navController = navController, productId = null)
            }
        }
        composable("edit_product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            BaseScreen(navController) {
                ProductEditScreen(navController = navController, productId = productId)
            }
        }
        composable(Screen.ManageEmployeeScreen.route) {
            BaseScreen(navController) {
                // Replace with your InventoryScreen when it's implemented
                ManageEmployeeScreen(navController = navController)
            }
        }
        composable("create_employee") {
            BaseScreen(navController) {
                EmployeeEditScreen(navController = navController, employeeId = null)
            }
        }
        composable("edit_employee/{employeeId}") { backStackEntry ->
            val employeeId = backStackEntry.arguments?.getString("employeeId")
            BaseScreen(navController) {
                EmployeeEditScreen(navController = navController, employeeId = employeeId)
            }
        }


        // Add other screens as needed
    }
}

sealed class Screen(val route: String) {

    object StoreScreen : Screen("store_screen")
    object MakeProductScreen : Screen("make_product_screen")
    object LoginScreen : Screen("login_screen")
    object InventoryScreen : Screen("inventory_screen")
    object HotFoodToGoScreen : Screen("hot_food_to_go_screen")
    object MakeSaladScreen : Screen("make_salad_screen")
    object AddToInventoryScreen : Screen("add_inventory_screen")
    object CheckInventoryScreen : Screen("check_inventory_screen")
    object ManageProductScreen : Screen("manage_product_screen")
    object ManageEmployeeScreen : Screen("manage_employee_screen")
    object ManagerScreen : Screen("manager_screen")

}