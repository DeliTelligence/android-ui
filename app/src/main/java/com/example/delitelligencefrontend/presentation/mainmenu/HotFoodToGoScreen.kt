/* https://www.youtube.com/watch?v=6_wK_Ud8--0&ab_channel=PhilippLackner
The Jetpack Compose Beginner Crash Course for 2023 💻 (Android Studio Tutorial)
Date 6/11/2024 accessed
All code here is adapted from the video*/

/*https://chatgpt.com
prompt: 'Create a left and right quadrant and have the left quadrant create a product card with a name and image placeholder for my list of objects
the right quadrant then build a button for finishing the product'
*/

package com.example.delitelligencefrontend.presentation.mainmenu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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


@Composable
fun HotFoodToGoScreen(
    productsViewModel: ProductsViewModel = hiltViewModel()
) {
    val hotFoodProducts by productsViewModel.hotFoodProductsFilling.collectAsState()
    val breakfastProducts by productsViewModel.breakfastProducts.collectAsState()
    val fillingProducts by productsViewModel.fillingProducts.collectAsState()
    val mainFillingProducts by productsViewModel.mainFillingProducts.collectAsState()

    val hotProducts = hotFoodProducts + breakfastProducts
    val coldProducts = mainFillingProducts + fillingProducts

    var currentDeliSale by remember { mutableStateOf<DeliSale?>(null) }
    var currentDeliProduct by remember { mutableStateOf<DeliProduct?>(null) }
    var finishedDeliProducts by remember { mutableStateOf<List<DeliProduct>>(emptyList()) }

    LaunchedEffect(Unit) {
        productsViewModel.fetchAllProducts()
    }

    LaunchedEffect(Unit) {
        val deliProduct: Product? = productsViewModel.getProductByName("Deli Bag")
        deliProduct?.let { product ->
            currentDeliProduct = DeliProduct(
                deliProduct = product,
                products = emptyList(),
                combinedWeight = 0.0, // This will be calculated later
                portionType = PortionType.QUANTITY
            )
        }
    }


    LaunchedEffect(currentDeliProduct) {
        currentDeliProduct?.let {
            currentDeliSale = DeliSale(
                productsViewModel.getEmployeeId().toString(),
                deliProduct = it,
                salePrice = it.calculateTotalPrice(),
                saleWeight = 0.0,
                wastePerSale = 0.0,
                wastePerSaleValue = 0.0,
                differenceWeight = 0.0,
                saleType = SaleType.HOT_FOOD,
                quantity = it.totalQuantity(),
                handMade = true
            )
        }
    }

    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Left quadrant: Show Hot Products and Cold Products as separate grids
        Column(modifier = Modifier.weight(0.6f).fillMaxHeight()) {
            // Hot Food section
            ProductGridQuadrant(
                title = "Hot Food",
                products = hotProducts,
                selectedProduct = currentDeliProduct?.products?.firstOrNull(),
                onProductSelected = { product ->
                    currentDeliProduct = currentDeliProduct?.copy(
                        products = currentDeliProduct!!.products + product,
                    ) ?: DeliProduct(
                        deliProduct = product,
                        products = listOf(product),
                        combinedWeight = 0.0,
                        portionType = PortionType.QUANTITY
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cold Products section
            ProductGridQuadrant(
                title = "Cold Products to Take",
                products = coldProducts,
                selectedProduct = currentDeliProduct?.products?.firstOrNull(),
                onProductSelected = { product ->
                    currentDeliProduct = currentDeliProduct?.copy(
                        products = currentDeliProduct!!.products + product,
                    ) ?: DeliProduct(
                        deliProduct = product,
                        products = listOf(product),
                        combinedWeight = 0.0,
                        portionType = PortionType.QUANTITY
                    )
                }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right quadrant: Controls like the finish action
        Column(
            modifier = Modifier.weight(0.4f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentDeliProduct != null) {
                Text(
                    text = "Total Price: $${String.format("%.2f", currentDeliProduct!!.calculateTotalPrice())}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        currentDeliProduct?.let {
                            finishedDeliProducts = finishedDeliProducts + it
                            currentDeliProduct = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
                ) {
                    Text(text = "Finish Product")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductGridQuadrant(
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
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), // Control height to prevent infinite constraints
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(products) { product ->
                HotFoodProductCard(
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
fun HotFoodProductCard(
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
                        Text("No Image", color = MaterialTheme.colorScheme.onSecondary)
                    }
                }
            }
        }
    }
}