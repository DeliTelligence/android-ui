/* https://www.youtube.com/watch?v=6_wK_Ud8--0&ab_channel=PhilippLackner
The Jetpack Compose Beginner Crash Course for 2023 💻 (Android Studio Tutorial)
Date 6/11/2024 accessed
All code here is adapted from the video*/

/*https://chatgpt.com
prompt: 'building a screen and dividing it into three quadrants top left, bottom left and Right and create product cards that I can fill with my objects
that show the name and image. Also create a circle shape on the right quadrant that I can use to store the weight data retrieved from the scales'
*/
package com.example.delitelligencefrontend.presentation.mainmenu

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.delitelligencefrontend.enumformodel.PortionType
import com.example.delitelligencefrontend.enumformodel.SaleType
import com.example.delitelligencefrontend.enumformodel.StandardType
import com.example.delitelligencefrontend.model.DeliProduct
import com.example.delitelligencefrontend.model.DeliSale
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.presentation.viewmodel.ProductsViewModel
import com.example.delitelligencefrontend.presentation.viewmodel.ScalesViewModel
import kotlin.math.abs

@Composable
fun MakeProductScreen(
    productsViewModel: ProductsViewModel = hiltViewModel(),
    scalesViewModel: ScalesViewModel = hiltViewModel()
) {
    val skipMainFillingProductIds = listOf(
        "44ead926-e583-4735-bc4d-721c17f5da27",
        "b64afc5e-911d-4a2a-875d-a37697f7a60d",
        "c8db3e8a-8edd-4b70-a0cd-4bfcf2d8b517"
    )

    val coldFoodProducts by productsViewModel.coldFoodProducts.collectAsState()
    val hotFoodProducts by productsViewModel.hotFoodProducts.collectAsState()
    val mainFillingProducts by productsViewModel.mainFillingProducts.collectAsState()
    val fillingProducts by productsViewModel.fillingProducts.collectAsState()
    val breakfastProducts by productsViewModel.breakfastProducts.collectAsState()

    var currentDeliSale by remember { mutableStateOf<DeliSale?>(null) }
    var currentDeliProduct by remember { mutableStateOf<DeliProduct?>(null) }
    var finishedDeliProducts by remember { mutableStateOf<List<DeliProduct>>(emptyList()) }

    LaunchedEffect(Unit) {
        productsViewModel.fetchAllProducts()
    }

    LaunchedEffect(currentDeliProduct) {
        Log.d("MakeProductScreen", "currentDeliProduct changed: $currentDeliProduct")
        if (currentDeliProduct != null) {
            currentDeliSale = DeliSale(
                productsViewModel.getEmployeeId().toString(),
                deliProduct = currentDeliProduct!!,
                salePrice = currentDeliProduct!!.calculateTotalPrice(),
                saleWeight = 0.0,
                wastePerSale = 0.0,
                wastePerSaleValue = 0.0,
                differenceWeight = 0.0,
                saleType = SaleType.HOT_FOOD,
                quantity = currentDeliProduct!!.totalQuantity(),
                handMade = true
            )
            scalesViewModel.setCurrentDeliSale(currentDeliSale!!)
        } else {
            currentDeliSale = null
        }
    }

    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Left side (2 quadrants)
        Column(modifier = Modifier.weight(1f)) {
            // Top left quadrant
            TopLeftQuadrant(
                coldFoodProducts = coldFoodProducts,
                hotFoodProducts = hotFoodProducts,
                selectedProduct = currentDeliProduct?.deliProduct,
                onProductSelected = { product ->
                    // Always create a new DeliProduct when a new product is selected
                    currentDeliProduct = DeliProduct(
                        deliProduct = product,
                        products = emptyList(),
                        combinedWeight = 0.0,
                        portionType = PortionType.FILLING
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom left quadrant
            currentDeliProduct?.let { deliProduct ->
                BottomLeftQuadrant(
                    skipMainFillingProductIds = skipMainFillingProductIds,
                    mainFillingProducts = mainFillingProducts,
                    fillingProducts = fillingProducts,
                    breakfastProducts = breakfastProducts,
                    currentDeliProduct = deliProduct,
                    onMainFillingSelected = { mainFilling ->
                        currentDeliProduct = deliProduct.copy(
                            products = deliProduct.products + mainFilling
                        )
                    },
                    onFillingSelected = { filling ->
                        currentDeliProduct = deliProduct.copy(
                            products = deliProduct.products + filling
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Right side (1 quadrant)
        Column(modifier = Modifier.weight(1f)) {
            RightQuadrant(
                currentDeliProduct = currentDeliProduct,
                productsViewModel = productsViewModel,
                scalesViewModel = scalesViewModel,
                onFinishProduct = {
                    currentDeliProduct?.let { deliProduct ->
                        finishedDeliProducts = finishedDeliProducts + deliProduct
                        currentDeliProduct = null
                    }
                }
            )
        }
    }
}

@Composable
fun TopLeftQuadrant(
    coldFoodProducts: List<Product>,
    hotFoodProducts: List<Product>,
    selectedProduct: Product?,
    onProductSelected: (Product) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    ) {
        LazyRow(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(coldFoodProducts) { product ->
                ProductCard(
                    product = product,
                    modifier = Modifier.padding(end = 16.dp),
                    isSelected = product == selectedProduct,
                    onProductSelected = onProductSelected
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(hotFoodProducts) { product ->
                ProductCard(
                    product = product,
                    modifier = Modifier.padding(end = 16.dp),
                    isSelected = product == selectedProduct,
                    onProductSelected = onProductSelected
                )
            }
        }
    }
}

@Composable
fun BottomLeftQuadrant(
    skipMainFillingProductIds: List<String>,
    mainFillingProducts: List<Product>,
    fillingProducts: List<Product>,
    breakfastProducts: List<Product>,
    currentDeliProduct: DeliProduct?,
    onMainFillingSelected: (Product) -> Unit,
    onFillingSelected: (Product) -> Unit
) {
    val shouldSkipMainFilling = currentDeliProduct?.deliProduct?.id in skipMainFillingProductIds
    val isBreakfastRoll = currentDeliProduct?.deliProduct?.productName == "Breakfast Roll"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when {
            isBreakfastRoll -> {
                Text("Select Breakfast Items", style = MaterialTheme.typography.headlineSmall)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(breakfastProducts) { product ->
                        if (currentDeliProduct != null) {
                            ProductCard(
                                product = product,
                                modifier = Modifier.padding(end = 16.dp),
                                isSelected = currentDeliProduct.products.contains(product),
                                onProductSelected = onFillingSelected
                            )
                        }
                    }
                }
            }
            currentDeliProduct?.deliProduct != null && currentDeliProduct.products.isEmpty() && !shouldSkipMainFilling -> {
                Text("Select Main Filling", style = MaterialTheme.typography.headlineSmall)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(mainFillingProducts) { product ->
                        ProductCard(
                            product = product,
                            modifier = Modifier.padding(end = 16.dp),
                            isSelected = false,
                            onProductSelected = onMainFillingSelected
                        )
                    }
                }
            }
            else -> {
                Text("Select Additional Fillings", style = MaterialTheme.typography.headlineSmall)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(fillingProducts) { product ->
                        ProductCard(
                            product = product,
                            modifier = Modifier.padding(end = 16.dp),
                            isSelected = currentDeliProduct?.products?.contains(product) == true,
                            onProductSelected = onFillingSelected
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun RightQuadrant(
    currentDeliProduct: DeliProduct?,
    productsViewModel: ProductsViewModel = hiltViewModel(),
    scalesViewModel: ScalesViewModel = hiltViewModel(),
    onFinishProduct: () -> Unit
) {
    // Observe states from the ViewModels
    val displayedValue by scalesViewModel.displayedValue.collectAsState()
    val isWeighing by scalesViewModel.isWeighing.collectAsState()
    val isWeightErrorSignificant by scalesViewModel.isWeightErrorSignificant.collectAsState()
    val currentDeliSale by scalesViewModel.currentDeliSale.collectAsState()

    // Use systemStartViewModel for scale connection and notification status
    val isScaleConnected by scalesViewModel.isScaleConnected.collectAsState()
    val areNotificationsOn by scalesViewModel.areNotificationsEnabled.collectAsState()
    val scaleStatus by scalesViewModel.scaleStatus.collectAsState()

    // State for weighing error message
    var weighingErrorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                val (value, isDifference) = displayedValue
                val text = when {
                    !isDifference -> String.format("%.2f", value)
                    isWeightErrorSignificant && value > 0 -> "+ ${String.format("%.2f", value)}"
                    isWeightErrorSignificant && value < 0 -> "- ${String.format("%.2f", abs(value))}"
                    else -> String.format("%.2f", value)
                }
                val color = when {
                    !isDifference -> Color.Black
                    isWeightErrorSignificant -> Color.Red
                    else -> Color.Black
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.displayMedium,
                    color = color
                )
            }
        }

        // Scale status
        Text(text = scaleStatus, style = MaterialTheme.typography.bodyMedium)

        // Weigh button
        Button(
            onClick = {
                if (isScaleConnected && areNotificationsOn && !isWeighing && currentDeliProduct != null) {
                    scalesViewModel.fetchWeightData(currentDeliProduct, StandardType.FILLING)
                    weighingErrorMessage = null // Clear error message if it's successful
                } else {
                    // Set error message when conditions are not met
                    weighingErrorMessage = "Weighing Error: Scale not connected or notifications not enabled"
                }
            },
            enabled = true, // Always enabled, decision handled inside onClick
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(if (isWeighing) "Weighing..." else "Weigh")
        }

        // Display weighing error if present
        weighingErrorMessage?.let {
            Text(
                text = it,
                color = Color.Red, // Display error in red
                style = MaterialTheme.typography.bodySmall
            )
        }

        // TARE button
        Button(
            onClick = { scalesViewModel.tareScale() },
            enabled = isScaleConnected,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("TARE")
        }

        // Price display
        Text(
            text = "Price: €${String.format("%.2f", currentDeliProduct?.calculateTotalPrice() ?: 0f)}",
            style = MaterialTheme.typography.titleLarge
        )

        // Finish Product button
        Button(
            onClick = {
                Log.d("FinishButton", "Button Clicked")
                currentDeliSale?.let {
                    Log.d("FinishButton", "Current DeliSale is not null: $it")
                    val updatedSale = productsViewModel.updateCurrentDeliSale(it)
                    Log.d("FinishButton", "Updated DeliSale: $updatedSale")
                    productsViewModel.updateCurrentDeliSale(updatedSale)
                    productsViewModel.postDeliSale(updatedSale)
                    onFinishProduct()
                } ?: run {
                    Log.d("FinishButton", "Current DeliSale is null")
                }
            },
            enabled = currentDeliProduct != null && displayedValue.first != 0.0,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Finish Product")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onProductSelected: (Product) -> Unit
) {
    Box(
        modifier = modifier
            .width(200.dp)
            .fillMaxHeight()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onProductSelected(product) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .alpha(if (isSelected) 1f else 0.6f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = product.productName ?: "Unknown",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            product.productImageDto?.let { base64Image ->
                val bitmap = base64Image.toByteArrayOrNull()?.toBitmapOrNull()
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = product.productName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Image", color = MaterialTheme.colorScheme.onSecondary)
                    }
                }
            }
        }
    }
}

fun String.toByteArrayOrNull(): ByteArray? = try {
    Base64.decode(this, Base64.DEFAULT)
} catch (e: IllegalArgumentException) {
    Log.e("Base64", "Decoding error", e)
    null
}

fun ByteArray.toBitmapOrNull(): Bitmap? = try {
    BitmapFactory.decodeByteArray(this, 0, this.size)
} catch (e: Exception) {
    Log.e("Bitmap", "Decoding error", e)
    null
}