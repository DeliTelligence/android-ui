package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAllProductsQuery
import com.example.delitelligence.GetAllSuppliersQuery
import com.example.delitelligencefrontend.model.Product
import com.example.delitelligencefrontend.model.Supplier

fun GetAllSuppliersQuery.GetAllSupplier.toSupplier(): Supplier {
    return Supplier(
        supplierName = this.supplierName

    )
}
