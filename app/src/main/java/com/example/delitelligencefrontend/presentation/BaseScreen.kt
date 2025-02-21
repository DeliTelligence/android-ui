package com.example.delitelligencefrontend.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.delitelligencefrontend.enumformodel.EmployeeTitle
import com.example.delitelligencefrontend.model.Employee
import com.example.delitelligencefrontend.model.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    navController: NavHostController,
    session: Session,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentEmployee by remember { mutableStateOf(session.getUser()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(navController, scope, drawerState, currentEmployee)
            }
        }
    ) {
        Scaffold(
            topBar = { TopBar(drawerState, scope) },
            bottomBar = { BottomBar(navController, currentEmployee) }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(drawerState: DrawerState, scope: CoroutineScope) {
    TopAppBar(
        title = { Text("Your App Name") },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        }
    )
}

@Composable
fun DrawerContent(
    navController: NavHostController,
    scope: CoroutineScope,
    drawerState: DrawerState,
    currentEmployee: Employee?
) {
    Column {
        Text("Navigation Drawer", modifier = Modifier.padding(16.dp))
        Divider()
        NavigationItem(navController, scope, drawerState, "Store", Icons.Filled.ShoppingCart, Screen.StoreScreen.route)
        NavigationItem(navController, scope, drawerState, "Inventory", Icons.Filled.List, Screen.InventoryScreen.route)
        if (currentEmployee?.employeeTitle == EmployeeTitle.MANAGER) {
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            NavigationItem(navController, scope, drawerState, "Manager Home", Icons.Filled.AccountCircle, Screen.ManagerScreen.route)
        }
    }
}

@Composable
fun NavigationItem(
    navController: NavHostController,
    scope: CoroutineScope,
    drawerState: DrawerState,
    title: String,
    icon: ImageVector,
    route: String
) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null) },
        label = { Text(title) },
        selected = navController.currentBackStackEntryAsState().value?.destination?.route == route,
        onClick = {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
            scope.launch {
                drawerState.close()
            }
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun BottomBar(navController: NavHostController, currentEmployee: Employee?) {
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        NavigationBarItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Store") },
            label = { Text("Store") },
            selected = currentRoute == Screen.StoreScreen.route,
            onClick = {
                navController.navigate(Screen.StoreScreen.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Inventory") },
            label = { Text("Inventory") },
            selected = currentRoute == Screen.InventoryScreen.route,
            onClick = {
                navController.navigate(Screen.InventoryScreen.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        if (currentEmployee?.employeeTitle == EmployeeTitle.MANAGER) {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Manager") },
                label = { Text("Manager") },
                selected = currentRoute == Screen.ManagerScreen.route,
                onClick = {
                    navController.navigate(Screen.ManagerScreen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
