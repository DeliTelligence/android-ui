/* https://www.youtube.com/watch?v=6_wK_Ud8--0&ab_channel=PhilippLackner
The Jetpack Compose Beginner Crash Course for 2023 💻 (Android Studio Tutorial)
Date 6/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.presentation.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.delitelligencefrontend.model.Inventory
import com.example.delitelligencefrontend.presentation.viewmodel.InventoryQueryViewModel
import com.example.delitelligencefrontend.presentation.viewmodel.ProductsViewModel

@Composable
fun CheckInventoryScreen(
    viewModel: InventoryQueryViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val inventoryList by viewModel.inventoryList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            label = { Text("Search Products") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Inventory list
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(inventoryList) { inventory ->
                InventoryItem(inventory = inventory)
            }
        }
    }
}

@Composable
fun InventoryItem(inventory: Inventory) {
    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = "Product Name: ${inventory.productName}", modifier = Modifier.weight(1f))
        Text(text = "Total Weight: ${inventory.totalWeight}", modifier = Modifier.weight(1f))
        Text(text = "Location: ${inventory.location}", modifier = Modifier.weight(1f))
        Text(text = "Inventory Value: ${inventory.inventoryValue}", modifier = Modifier.weight(1f))
    }
}
