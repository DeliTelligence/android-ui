/* https://www.youtube.com/watch?v=6_wK_Ud8--0&ab_channel=PhilippLackner
The Jetpack Compose Beginner Crash Course for 2023 💻 (Android Studio Tutorial)
Date 6/11/2024 accessed
All code here is adapted from the video*/

/*https://chatgpt.com
prompt: 'building a screen and dividing it into three quadrants top left, bottom left and Right and create product cards that I can fill with my objects
that show the name and image. Also create a circle shape on the right quadrant that I can use to store the weight data retrieved from the scales'
Used the code from the prompt to make the similar elements.
*/



package com.example.delitelligencefrontend.presentation.mainmenu

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.delitelligencefrontend.model.DeliProduct
import com.example.delitelligencefrontend.model.DeliSale
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.presentation.viewmodel.ProductsViewModel
import java.util.UUID
import kotlin.math.abs

@Composable
fun MakeSaladScreen(
    productsViewModel: ProductsViewModel = hiltViewModel()
) {
    val hotFoodProducts by productsViewModel.hotFoodProductsFilling.collectAsState()
    val mainFillingProducts by productsViewModel.mainFillingProducts.collectAsState()
    val coldFoodProducts by productsViewModel.fillingProducts.collectAsState()

    val allSaladProducts = hotFoodProducts + mainFillingProducts + coldFoodProducts  // Join the three lists

    var currentDeliSale by remember { mutableStateOf<DeliSale?>(null) }
    var currentDeliProduct by remember { mutableStateOf<DeliProduct?>(null) }
    var finishedDeliProducts by remember { mutableStateOf<List<DeliProduct>>(emptyList()) }

    LaunchedEffect(Unit) {
        productsViewModel.fetchAllProducts()
    }

    LaunchedEffect(currentDeliProduct) {
        if (currentDeliProduct != null) {
            currentDeliSale = DeliSale(
                productsViewModel.getEmployeeId().toString(),
                deliProduct = currentDeliProduct!!,
                salePrice = currentDeliProduct!!.calculateTotalPrice(),
                saleWeight = 0.0, // Placeholder, you will set it when fetching weight data
                wastePerSale = 0.0,
                wastePerSaleValue = 0.0,
                differenceWeight = 0.0,
                saleType = SaleType.HOT_FOOD, // Adjust as per your logic
                quantity = currentDeliProduct!!.totalQuantity(), // Assuming 1 for simplicity
                handMade = true // Set based on your logic
            )
        } else {
            currentDeliSale = null
        }
    }

    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Left side: Combined product list in a vertical scrollable grid
        Column(modifier = Modifier
            .weight(0.6f)
            .fillMaxHeight()
        ) {
            SaladProductGrid(
                title = "Salad Fillings",
                products = allSaladProducts,
                selectedProduct = currentDeliProduct?.products?.firstOrNull(),
                onProductSelected = { product ->
                    currentDeliProduct = currentDeliProduct?.copy(
                        products = currentDeliProduct!!.products + product,
                    ) ?: DeliProduct(
                        deliProduct = productsViewModel.allProducts.value.firstOrNull { it.id == "cdc8ebe2-fd1b-4cd3-80dd-cbc3e9d1fec5" },
                        products = listOf(product),
                        combinedWeight = 0.0,
                        portionType = PortionType.SALAD
                    )
                }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right side: Scale interaction and Finish Product button
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SaladRightQuadrant(
                currentDeliProduct = currentDeliProduct,
                currentDeliSale = currentDeliSale,
                productsViewModel = productsViewModel,
                onFinishProduct = {
                    currentDeliProduct?.let {
                        finishedDeliProducts = finishedDeliProducts + it
                        currentDeliProduct = null
                        currentDeliSale = null
                    }
                }
            )
        }
    }
}

@Composable
fun SaladRightQuadrant(
    currentDeliProduct: DeliProduct?,
    currentDeliSale: DeliSale?,
    productsViewModel: ProductsViewModel = hiltViewModel(),
    onFinishProduct: () -> Unit
) {
    val displayedWeight by productsViewModel.displayedValue.collectAsState()
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
        // Weight display circle
        Box(
            modifier = Modifier
                .size(250.dp)  // Adjusted size
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(230.dp)  // Adjusted size
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                val (value, isDifference) = displayedWeight
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
                    style = MaterialTheme.typography.displayMedium,  // Adjusted text size
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
                .height(48.dp)  // Adjusted height
        ) {
            Text(if (isWeighing) "Weighing..." else "Weigh")
        }

        // TARE button
        Button(
            onClick = { productsViewModel.tareScale() },
            enabled = isScaleConnected,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)  // Adjusted height
        ) {
            Text("TARE")
        }

        // Price display
        Text(
            text = "Price: $${String.format("%.2f", currentDeliProduct?.calculateTotalPrice() ?: 0f)}",
            style = MaterialTheme.typography.titleLarge
        )

        // Finish Product button, shown once weighing is done
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
            enabled = currentDeliProduct != null && displayedWeight.first != 0.0,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Finish Product")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SaladProductGrid(
    title: String,
    products: List<Product>,
    selectedProduct: Product?,
    onProductSelected: (Product) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),  // 3 items per row
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(products) { product ->
                SaladProductCard(
                    product = product,
                    modifier = Modifier.padding(8.dp),
                    isSelected = product == selectedProduct,
                    onProductSelected = onProductSelected
                )
            }
        }
    }
}

@Composable
fun SaladProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onProductSelected: (Product) -> Unit
) {
    Box(
        modifier = modifier
            .width(120.dp)
            .height(120.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onProductSelected(product) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .alpha(if (isSelected) 1f else 0.6f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = product.productName ?: "Unknown",
                style = MaterialTheme.typography.titleSmall,
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
                        Text(
                            text = "No Image",
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }
    }
}