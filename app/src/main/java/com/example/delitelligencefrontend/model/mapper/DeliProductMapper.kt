/*https://medium.com/@zorbeytorunoglu/mapstruct-android-872b54a4dd10
* MapStruct Android
* All code is adapted from the article
* accessed on the 14/01/2025*/
package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.type.DeliProductInput
import com.example.delitelligencefrontend.model.DeliProduct
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "default", uses = [ProductMapperStruct::class])
interface DeliProductMapper {

    companion object {
        val INSTANCE: DeliProductMapper = Mappers.getMapper(DeliProductMapper::class.java)
    }

    @Mapping(target = "productInputDtos", source = "products")
    @Mapping(target = "productInputDto", source = "deliProduct")
    @Mapping(target = "combinedWeight", source = "combinedWeight")
    @Mapping(target = "portionType", source = "portionType")
    fun toDeliProductInput(deliProduct: DeliProduct): DeliProductInput
}