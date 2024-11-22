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
import com.example.delitelligencefrontend.model.DeliProduct
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.presentation.viewmodel.ProductsViewModel
import java.util.UUID
import kotlin.math.abs

@Composable
fun MakeProductScreen(
    productsViewModel: ProductsViewModel = hiltViewModel()
) {
    val coldFoodProducts by productsViewModel.coldFoodProducts.collectAsState()
    val hotFoodProducts by productsViewModel.hotFoodProducts.collectAsState()
    val mainFillingProducts by productsViewModel.mainFillingProducts.collectAsState()
    val fillingProducts by productsViewModel.fillingProducts.collectAsState()

    var currentDeliProduct by remember { mutableStateOf<DeliProduct?>(null) }
    var finishedDeliProducts by remember { mutableStateOf<List<DeliProduct>>(emptyList()) }

    LaunchedEffect(Unit) {
        productsViewModel.fetchAllProducts()
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left side (2 quadrants)
        Column(modifier = Modifier.weight(1f)) {
            // Top left quadrant
            TopLeftQuadrant(
                coldFoodProducts = coldFoodProducts,
                hotFoodProducts = hotFoodProducts,
                selectedProduct = currentDeliProduct?.products?.firstOrNull(),
                onProductSelected = { product ->
                    currentDeliProduct = DeliProduct(
                        deliProductId = UUID.randomUUID().toString(),
                        products = listOf(product),
                        combinedWeight = 0f,
                        deliProductPrice = product.productPrice?.toFloat() ?: 0f,
                        deliProductQuantity = 1,
                        weightToPrice = false
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom left quadrant
            if (currentDeliProduct != null) {
                BottomLeftQuadrant(
                    mainFillingProducts = mainFillingProducts,
                    fillingProducts = fillingProducts,
                    currentDeliProduct = currentDeliProduct,
                    onMainFillingSelected = { mainFilling ->
                        currentDeliProduct = currentDeliProduct?.copy(
                            products = currentDeliProduct!!.products + mainFilling,
                            deliProductPrice = currentDeliProduct!!.deliProductPrice + (mainFilling.productPrice?.toFloat() ?: 0f)
                        )
                    },
                    onFillingSelected = { filling ->
                        currentDeliProduct = currentDeliProduct?.copy(
                            products = currentDeliProduct!!.products + filling,
                            deliProductPrice = currentDeliProduct!!.deliProductPrice + (filling.productPrice?.toFloat() ?: 0f)
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
                onFinishProduct = {
                    currentDeliProduct?.let { deliProduct ->
                        finishedDeliProducts = finishedDeliProducts + deliProduct
                        // Create a new DeliProduct
                        currentDeliProduct = DeliProduct(
                            deliProductId = UUID.randomUUID().toString(),
                            products = emptyList(),
                            combinedWeight = 0f,
                            deliProductPrice = 0f,
                            deliProductQuantity = 1,
                            weightToPrice = false
                        )
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
    mainFillingProducts: List<Product>,
    fillingProducts: List<Product>,
    currentDeliProduct: DeliProduct?,
    onMainFillingSelected: (Product) -> Unit,
    onFillingSelected: (Product) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (currentDeliProduct?.products?.size == 1) {
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
        } else {
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

@Composable
fun RightQuadrant(
    currentDeliProduct: DeliProduct?,
    productsViewModel: ProductsViewModel = hiltViewModel(),
    onFinishProduct: () -> Unit
) {
    val displayedValue by productsViewModel.displayedValue.collectAsState()
    val isScaleConnected by productsViewModel.isScaleConnected.collectAsState()
    val isWeighing by productsViewModel.isWeighing.collectAsState()
    val scaleStatus by productsViewModel.scaleStatus.collectAsState()
    val isWeightErrorSignificant by productsViewModel.isWeightErrorSignificant.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Weight/Difference display circle
        Box(
            modifier = Modifier
                .size(250.dp)  // Reduced size
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(230.dp)  // Reduced size
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
                    style = MaterialTheme.typography.displayMedium,  // Reduced text size
                    color = color
                )
            }
        }

        // Scale status
        Text(text = scaleStatus, style = MaterialTheme.typography.bodyMedium)

        // Weigh button
        Button(
            onClick = {
                currentDeliProduct?.let { productsViewModel.fetchWeightData(it) }
            },
            enabled = isScaleConnected && !isWeighing && currentDeliProduct != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)  // Reduced height
        ) {
            Text(if (isWeighing) "Weighing..." else "Weigh")
        }

        // TARE button
        Button(
            onClick = { productsViewModel.tareScale() },
            enabled = isScaleConnected,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)  // Reduced height
        ) {
            Text("TARE")
        }

        // Price display
        Text(
            text = "Price: $${String.format("%.2f", currentDeliProduct?.deliProductPrice ?: 0f)}",
            style = MaterialTheme.typography.titleLarge
        )

        // Finish Product button
        Button(
            onClick = onFinishProduct,
            enabled = currentDeliProduct != null && displayedValue.first != 0.0,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)  // Reduced height
        ) {
            Text("Finish Product")
        }

        // Add some extra space at the bottom to ensure everything is visible
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

            product.productImage?.let { base64Image ->
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

// Extension function to convert ByteArray to Bitmap
fun ByteArray.toBitmapOrNull(): Bitmap? = try {
    BitmapFactory.decodeByteArray(this, 0, this.size)
} catch (e: Exception) {
    Log.e("Bitmap", "Decoding error", e)
    null
}

// Extension function to safely decode a Base64 string to ByteArray
