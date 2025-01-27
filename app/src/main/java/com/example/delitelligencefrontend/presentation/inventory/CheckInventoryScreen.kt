/* https://www.youtube.com/watch?v=6_wK_Ud8--0&ab_channel=PhilippLackner
The Jetpack Compose Beginner Crash Course for 2023 💻 (Android Studio Tutorial)
Date 6/11/2024 accessed
All code here is adapted from the video*/

package com.example.delitelligencefrontend.presentation.inventory

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.delitelligencefrontend.model.Inventory
import com.example.delitelligencefrontend.presentation.mainmenu.toBitmapOrNull
import com.example.delitelligencefrontend.presentation.mainmenu.toByteArrayOrNull
import com.example.delitelligencefrontend.presentation.viewmodel.InventoryQueryViewModel
import kotlin.math.roundToInt
import com.example.delitelligencefrontend.helper.HelperMethods.Companion.toCurrency

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
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            label = { Text("Search Products") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(inventoryList) { inventory ->
                InventoryItem(inventory = inventory)
                Divider()
            }
        }
    }
}

@Composable
fun InventoryItem(inventory: Inventory) {
    inventory.products.forEach { product ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            product.productImage.toByteArrayOrNull()?.toBitmapOrNull()?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Product Information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = product.productName, modifier = Modifier.weight(1f))
                Text(text = "Value: ${inventory.inventoryValue.toCurrency("EUR")}", modifier = Modifier.weight(1f))
                Text(text = "Filling Portions Left: ${inventory.fillingPortion.roundToInt()}", modifier = Modifier.weight(1f))
                Text(text = "Salad Portions Left: ${inventory.saladPortion.roundToInt()}", modifier = Modifier.weight(1f))
            }
        }
    }
}
