/* https://www.youtube.com/watch?v=6_wK_Ud8--0&ab_channel=PhilippLackner
The Jetpack Compose Beginner Crash Course for 2023 💻 (Android Studio Tutorial)
Date 6/11/2024 accessed
All code here is adapted from the video*/


package com.example.delitelligencefrontend.presentation.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.delitelligencefrontend.enumformodel.AdjustmentType
import com.example.delitelligencefrontend.enumformodel.StandardType
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.presentation.viewmodel.InventoryAdjustmentViewModel
import com.example.delitelligencefrontend.presentation.viewmodel.ScalesViewModel

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
    val selectedProduct by viewModel.selectedProduct.collectAsState()

    val scalesViewModel: ScalesViewModel = hiltViewModel()

    var expandedAdjustmentType by remember { mutableStateOf(false) }
    var expandedSupplier by remember { mutableStateOf(false) }
    var expandedProduct by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            // Adjustment Type Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedAdjustmentType,
                onExpandedChange = { expandedAdjustmentType = !expandedAdjustmentType }
            ) {
                TextField(
                    value = adjustmentType.name,
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
        }

        if (adjustmentType != null) {
            item {
                // Supplier Dropdown
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
            }

            item {
                // Product Dropdown
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
                                    viewModel.updateSelectedProduct(product) // Store the product
                                    expandedProduct = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (adjustmentType == AdjustmentType.WASTE) {
                item {
                    // Reason TextField
                    TextField(
                        value = reason.orEmpty(),
                        onValueChange = { viewModel.updateReason(it) },
                        label = { Text("Reason") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Weighing UI below the reason
                    WeighingUI(scalesViewModel = scalesViewModel, selectedProduct = selectedProduct, inventoryViewModel = viewModel)
                }
            } else {
                item {
                    // Manual Weight Input
                    TextField(
                        value = orderWeight.toString(),
                        onValueChange = { newWeight ->
                            val weight = newWeight.toDoubleOrNull() ?: 0.0
                            viewModel.updateOrderWeight(weight)
                            if (adjustmentType == AdjustmentType.DELIVERY) {
                                selectedProduct?.let {
                                    viewModel.updateCostPerBox((it.productPrice ?: 0.0) * weight)
                                }
                            }
                        },
                        label = { Text("Product Weight in grams") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (adjustmentType == AdjustmentType.DELIVERY) {
                item {
                    TextField(
                        value = costPerBox.toString(),
                        onValueChange = { viewModel.updateCostPerBox(it.toDoubleOrNull() ?: 0.0) },
                        label = { Text("Cost of Product") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { viewModel.submitAdjustment() }) {
                    Text("Finish")
                }
            }
        }
    }
}

@Composable
fun WeighingUI(
    scalesViewModel: ScalesViewModel,
    selectedProduct: Product?,
    inventoryViewModel: InventoryAdjustmentViewModel
) {
    if (selectedProduct == null) return

    val displayedValue by scalesViewModel.displayedValue.collectAsState()
    val isWeighing by scalesViewModel.isWeighing.collectAsState()
    val scaleStatus by scalesViewModel.scaleStatus.collectAsState()
    val isScaleConnected by scalesViewModel.isScaleConnected.collectAsState()
    val areNotificationsOn by scalesViewModel.areNotificationsEnabled.collectAsState()

    var weighingErrorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(230.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                val (value, _) = displayedValue
                // Format the weight to be center aligned, appending "g"
                val text = String.format("%.2f g", value)
                Text(
                    text = text,
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center) // Ensure the text is centered
                )
            }
        }

        Text(text = scaleStatus, style = MaterialTheme.typography.bodyMedium)

        Button(
            onClick = {
                if (isScaleConnected && areNotificationsOn && !isWeighing) {
                    scalesViewModel.fetchWeightData { weight ->
                        inventoryViewModel.updateOrderWeight(weight)
                    }
                    weighingErrorMessage = null
                } else {
                    weighingErrorMessage = "Weighing Error: Scale not connected or notifications not enabled"
                }
            },
            enabled = true,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(if (isWeighing) "Weighing..." else "Weigh")
        }

        weighingErrorMessage?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Button(
            onClick = { scalesViewModel.tareScale() },
            enabled = isScaleConnected,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("TARE")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}