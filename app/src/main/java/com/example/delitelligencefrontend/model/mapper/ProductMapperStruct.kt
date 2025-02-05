/*https://medium.com/@zorbeytorunoglu/mapstruct-android-872b54a4dd10
* MapStruct Android
* All code is adapted from the article
* accessed on the 14/01/2025*/


package com.example.delitelligencefrontend.model.mapper

import com.apollographql.apollo.api.Optional
import com.example.delitelligence.type.ProductInputDto
import com.example.delitelligencefrontend.model.Product
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "default")
interface ProductMapperStruct {

    companion object {
        val INSTANCE: ProductMapperStruct = Mappers.getMapper(ProductMapperStruct::class.java)
    }

    // Map Product to ProductInputDto

    fun toProductInputDto(product: Product): ProductInputDto


}