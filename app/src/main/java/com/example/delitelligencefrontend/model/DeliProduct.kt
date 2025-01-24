package com.example.delitelligencefrontend.model

import com.example.delitelligencefrontend.enumformodel.PortionType

data class DeliProduct(
    val deliProduct: Product?,
    val products: List<Product>,
    val combinedWeight: Double,
    val portionType: PortionType
) {
    fun calculateTotalPrice(): Double {
        val mainProductPrice = deliProduct?.productPrice?: 0.0
        val additionalProductsPrice = products.fold(0.0) { sum, product ->
            sum + (product.productPrice?.toDouble() ?: 0.0)
        }
        return mainProductPrice + additionalProductsPrice
    }

    fun totalQuantity(): Int {
        val mainProductQuantity = if (deliProduct != null) 1 else 0
        val additionalProductsQuantity = products.size
        return mainProductQuantity + additionalProductsQuantity
    }
}
