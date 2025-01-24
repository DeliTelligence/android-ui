/*https://medium.com/@zorbeytorunoglu/mapstruct-android-872b54a4dd10
* MapStruct Android
* All code is adapted from the article
* accessed on the 14/01/2025*/

package com.example.delitelligencefrontend.model.mapper

import com.apollographql.apollo.api.Optional
import com.example.delitelligence.type.DeliSaleInputDto
import com.example.delitelligencefrontend.enumformodel.SaleType
import com.example.delitelligencefrontend.model.DeliSale
import com.example.delitelligence.type.SaleType as GraphQLSaleType
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "default", uses = [DeliProductMapper::class])
interface DeliSaleMapper {

    companion object {
        val INSTANCE: DeliSaleMapper = Mappers.getMapper(DeliSaleMapper::class.java)
    }

    @Mapping(target = "employeeId", source = "employeeId")
    @Mapping(target = "deliProductInputDto", source = "deliProduct")
    @Mapping(target = "saleType", source = "saleType",  qualifiedByName = ["mapSaleType"])
    @Mapping(target = "salePrice", source = "salePrice")
    @Mapping(target = "saleWeight", source = "saleWeight")
    @Mapping(target = "wastePerSale", source = "wastePerSale")
    @Mapping(target = "wastePerSaleValue", source = "wastePerSaleValue")
    @Mapping(target = "differenceWeight", source = "differenceWeight")
    fun toDeliSaleInputDto(deliSale: DeliSale): DeliSaleInputDto


    @Named("mapSaleType")
    fun mapSaleType(saleType: SaleType): GraphQLSaleType {
        return when (saleType) {
            SaleType.COLD_FOOD -> GraphQLSaleType.COLD_FOOD
            SaleType.HOT_FOOD -> GraphQLSaleType.HOT_FOOD
            SaleType.SALAD -> GraphQLSaleType.SALAD
            else -> throw IllegalArgumentException("Unhandled sale type: $saleType")
        }
    }


}