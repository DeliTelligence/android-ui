/* https://www.youtube.com/watch?v=6_wK_Ud8--0&ab_channel=PhilippLackner
The Jetpack Compose Beginner Crash Course for 2023 💻 (Android Studio Tutorial)
Date 6/11/2024 accessed
All code here is adapted from the video*/


package com.example.delitelligencefrontend.presentation.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.delitelligencefrontend.enumformodel.AdjustmentType
import com.example.delitelligencefrontend.presentation.viewmodel.InventoryAdjustmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToInventoryScreen(viewModel: InventoryAdjustmentViewModel = hiltViewModel()) {
    val adjustmentType by viewModel.adjustmentType.collectAsState()
    val supplierName by viewModel.supplierName.collectAsState()
    val productName by viewModel.productName.collectAsState()
    val orderWeight by viewModel.orderWeight.collectAsState()
    val costPerBox by viewModel.costPerBox.collectAsState()
    val reason by viewModel.reason.collectAsState()
    val suppliers by viewModel.suppliers.collectAsState()
    val products by viewModel.products.collectAsState()

    var expandedAdjustmentType by remember { mutableStateOf(false) }
    var expandedSupplier by remember { mutableStateOf(false) }
    var expandedProduct by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Adjustment Type dropdown
        ExposedDropdownMenuBox(
            expanded = expandedAdjustmentType,
            onExpandedChange = { expandedAdjustmentType = !expandedAdjustmentType }
        ) {
            TextField(
                value = adjustmentType.name, // Display currently selected adjustment type
                onValueChange = {},
                label = { Text("Adjustment Type") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAdjustmentType)
                },
                readOnly = true,
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expandedAdjustmentType,
                onDismissRequest = { expandedAdjustmentType = false }
            ) {
                AdjustmentType.values().forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            viewModel.updateAdjustmentType(type)
                            expandedAdjustmentType = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (adjustmentType != null) {
            // Supplier dropdown
            ExposedDropdownMenuBox(
                expanded = expandedSupplier,
                onExpandedChange = { expandedSupplier = !expandedSupplier }
            ) {
                TextField(
                    value = supplierName,
                    onValueChange = { viewModel.updateSupplierName(it) },
                    label = { Text("Supplier Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSupplier)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedSupplier,
                    onDismissRequest = { expandedSupplier = false }
                ) {
                    suppliers.forEach { supplier ->
                        DropdownMenuItem(
                            text = { Text(supplier.supplierName) },
                            onClick = {
                                viewModel.updateSupplierName(supplier.supplierName)
                                expandedSupplier = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product dropdown
            ExposedDropdownMenuBox(
                expanded = expandedProduct,
                onExpandedChange = { expandedProduct = !expandedProduct }
            ) {
                TextField(
                    value = productName,
                    onValueChange = { viewModel.updateProductName(it) },
                    label = { Text("Product Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProduct)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedProduct,
                    onDismissRequest = { expandedProduct = false }
                ) {
                    products.forEach { product ->
                        DropdownMenuItem(
                            text = { Text(product.productName ?: "Unknown Product") },
                            onClick = {
                                product.productName?.let {
                                    viewModel.updateProductName(it)
                                    // Automatically set cost per box if it's a waste adjustment
                                    if (adjustmentType == AdjustmentType.WASTE) {
                                        viewModel.updateCostPerBox((product.productPrice ?: 0.0) * orderWeight)
                                    }
                                }
                                expandedProduct = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = orderWeight.toString(),
                onValueChange = { newWeight ->
                    val weight = newWeight.toDoubleOrNull() ?: 0.0
                    viewModel.updateOrderWeight(weight)
                    // Automatically update cost per box if it's a waste adjustment
                    if (adjustmentType == AdjustmentType.WASTE) {
                        // Find the selected product
                        val selectedProduct = products.find { it.productName == productName }
                        selectedProduct?.let { product ->
                            viewModel.updateCostPerBox((product.productPrice ?: 0.0) * weight)
                        }
                    }
                },
                label = { Text("Order Weight") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (adjustmentType == AdjustmentType.DELIVERY) {
                TextField(
                    value = costPerBox.toString(),
                    onValueChange = { viewModel.updateCostPerBox(it.toDoubleOrNull() ?: 0.0) },
                    label = { Text("Cost Per Box") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (adjustmentType == AdjustmentType.WASTE) {
                TextField(
                    value = reason.orEmpty(),
                    onValueChange = { viewModel.updateReason(it) },
                    label = { Text("Reason") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.submitAdjustment() }) {
                Text("Submit Adjustment")
            }
        }
    }
}