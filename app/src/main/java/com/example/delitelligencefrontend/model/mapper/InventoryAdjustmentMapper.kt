/*https://medium.com/@zorbeytorunoglu/mapstruct-android-872b54a4dd10
* MapStruct Android
* All code is adapted from the article
* accessed on the 14/01/2025*/

package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.type.InventoryAdjustmentInputDto
import com.example.delitelligencefrontend.enumformodel.AdjustmentType
import com.example.delitelligencefrontend.model.*
import com.example.delitelligencefrontend.model.mapper.ProductMapperStruct
import com.example.delitelligence.type.AdjustmentType as GraphQLAdjustmentType
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import java.util.Optional

@Mapper(componentModel = "default")
interface InventoryAdjustmentMapper {

    companion object {
        val INSTANCE: InventoryAdjustmentMapper = Mappers.getMapper(InventoryAdjustmentMapper::class.java)
    }

    @Mappings(
        Mapping(target = "supplierName", source = "supplierName"),
        Mapping(target = "productName", source = "productName"),
        Mapping(target = "orderWeight", source = "orderWeight"),
        Mapping(target = "costPerBox", source = "costPerBox"),
        Mapping(target = "adjustmentType", source = "adjustmentType", qualifiedByName = ["mapAdjustmentType"]),
        Mapping(target = "reason", source = "reason")
    )
    fun toInventoryAdjustmentInputDto(input: InventoryAdjustment): InventoryAdjustmentInputDto


    @Named("mapAdjustmentType")
    fun mapAdjustmentType(adjustmentType: AdjustmentType): GraphQLAdjustmentType {
        return when (adjustmentType) {
            AdjustmentType.WASTE -> GraphQLAdjustmentType.WASTE
            AdjustmentType.DELIVERY -> GraphQLAdjustmentType.DELIVERY
            AdjustmentType.USED -> GraphQLAdjustmentType.USED
        }
    }


}