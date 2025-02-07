package com.example.delitelligencefrontend.presentation.managerscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.delitelligence.type.StandardType
import com.example.delitelligencefrontend.enumformodel.ProductType
import com.example.delitelligencefrontend.model.StandardWeight
import com.example.delitelligencefrontend.model.StandardWeightProduct
import com.example.delitelligencefrontend.modeldto.product.*
import com.example.delitelligencefrontend.presentation.viewmodel.ManageProductViewModel
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(
    navController: NavController,
    productId: String?,
    viewModel: ManageProductViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
    val product = products.find { it.id == productId }

    var productName by remember { mutableStateOf(product?.productName ?: "") }
    var standardWeightProducts by remember {
        mutableStateOf(
            product?.standardWeightProducts?.map {
                StandardWeightProduct(
                    standardWeight = it.standardWeight,
                    standardWeightValue = it.standardWeightValue
                )
            } ?: listOf(
                StandardWeightProduct(
                    standardWeight = StandardWeight("", com.example.delitelligencefrontend.enumformodel.StandardType.UNKNOWN),
                    standardWeightValue = 0f
                )
            )
        )
    }
    var productPrice by remember { mutableStateOf(product?.productPrice ?: 0.0) }
    var productImageDto by remember { mutableStateOf(product?.productImageDto ?: "") }
    var productDescription by remember { mutableStateOf(product?.productDescription ?: "") }
    var productType by remember { mutableStateOf(product?.productType ?: ProductType.HOT_FOOD) }

    var expandedProductType by remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun Bitmap.toBase64(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun String.toByteArrayOrNull(): ByteArray? = try {
        Base64.decode(this, Base64.DEFAULT)
    } catch (e: IllegalArgumentException) {
        null
    }

    fun ByteArray.toBitmapOrNull(): Bitmap? = try {
        BitmapFactory.decodeByteArray(this, 0, this.size)
    } catch (e: Exception) {
        null
    }

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val inputStream = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                productImageDto = bitmap?.toBase64() ?: ""
                inputStream?.close()
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (productId == null) "Create Product" else "Edit Product")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = productPrice.toString(),
                onValueChange = { productPrice = it.toDoubleOrNull() ?: 0.0 },
                label = { Text("Product Price") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = productDescription,
                onValueChange = { productDescription = it },
                label = { Text("Product Description") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedProductType,
                onExpandedChange = { expandedProductType = !expandedProductType }
            ) {
                TextField(
                    value = productType.name,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Product Type") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedProductType,
                    onDismissRequest = { expandedProductType = false }
                ) {
                    ProductType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                productType = type
                                expandedProductType = false
                            }
                        )
                    }
                }
            }

            // Standard Weight Products
            Text("Standard Weight Products", style = MaterialTheme.typography.titleMedium)
            standardWeightProducts.forEachIndexed { index, standardWeightProduct ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var expandedStandardType by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedStandardType,
                        onExpandedChange = { expandedStandardType = !expandedStandardType }
                    ) {
                        TextField(
                            value = standardWeightProduct.standardWeight?.standardType?.name ?: "",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Type") },
                            modifier = Modifier
                                .weight(1f)
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedStandardType,
                            onDismissRequest = { expandedStandardType = false }
                        ) {
                            com.example.delitelligencefrontend.enumformodel.StandardType.values().forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name) },
                                    onClick = {
                                        val updatedList = standardWeightProducts.toMutableList()
                                        updatedList[index] = standardWeightProduct.copy(
                                            standardWeight = standardWeightProduct.standardWeight?.copy(
                                                standardType = type
                                            )
                                        )
                                        standardWeightProducts = updatedList
                                        expandedStandardType = false
                                    }
                                )
                            }
                        }
                    }
                    TextField(
                        value = standardWeightProduct.standardWeightValue?.toString() ?: "",
                        onValueChange = { newValue ->
                            val updatedList = standardWeightProducts.toMutableList()
                            updatedList[index] = standardWeightProduct.copy(
                                standardWeightValue = newValue.toFloatOrNull() ?: 0f
                            )
                            standardWeightProducts = updatedList
                        },
                        label = { Text("Weight") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Button(
                onClick = {
                    standardWeightProducts = standardWeightProducts + StandardWeightProduct(
                        standardWeight = StandardWeight("", com.example.delitelligencefrontend.enumformodel.StandardType.UNKNOWN),
                        standardWeightValue = 0f
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Standard Weight")
            }

            // Image selection
            Text("Product Image", style = MaterialTheme.typography.titleMedium)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        selectImageLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                if (productImageDto.isNotEmpty()) {
                    val byteArray = productImageDto.toByteArrayOrNull()
                    byteArray?.let { array ->
                        val bitmap = array.toBitmapOrNull()
                        bitmap?.let { imageBitmap ->
                            Image(
                                bitmap = imageBitmap.asImageBitmap(),
                                contentDescription = "Product Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                } else {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Select Image",
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
            }

            Button(
                onClick = {
                    if (productId == null) {
                        viewModel.createProduct(
                            ProductCreate(
                                productName = productName,
                                standardWeightProducts = standardWeightProducts.map {
                                    StandardWeightProductCreate(
                                        standardWeightValue = it.standardWeightValue ?: 0f,
                                        standardWeight = StandardWeightCreate(
                                            standardWeightId = it.standardWeight?.standardWeightId ?: ""
                                        )
                                    )
                                },
                                productPrice = productPrice,
                                productImageDto = productImageDto,
                                productDescription = productDescription,
                                productType = productType
                            )
                        )
                    } else {
                        viewModel.updateProduct(
                            ProductUpdate(
                                id = productId,
                                productName = productName,
                                standardWeightProducts = standardWeightProducts.map {
                                    StandardWeightProductUpdate(
                                        standardWeightValue = it.standardWeightValue ?: 0f,
                                        standardWeight = StandardWeightUpdate(
                                            standardWeightId = it.standardWeight?.standardWeightId ?: ""
                                        )
                                    )
                                },
                                productPrice = productPrice,
                                productImageDto = productImageDto,
                                productDescription = productDescription,
                                productType = productType
                            )
                        )
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (productId == null) "Create" else "Update")
            }
        }
    }
}