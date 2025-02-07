package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.GetAllProductsQuery
import com.example.delitelligence.type.StandardWeightInputCreateDto
import com.example.delitelligence.type.StandardWeightInputUpdateDto
import com.example.delitelligencefrontend.model.StandardWeight
import com.example.delitelligencefrontend.modeldto.product.StandardWeightCreate
import com.example.delitelligencefrontend.modeldto.product.StandardWeightUpdate
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "default")
interface StandardWeightMapper {

    companion object {
        val INSTANCE: StandardWeightMapper = Mappers.getMapper(StandardWeightMapper::class.java)
    }

    fun toStandardWeightCreateDto(standardWeight: StandardWeightCreate): StandardWeightInputCreateDto

    fun toStandardWeightUpdateDto(standardWeight: StandardWeightUpdate): StandardWeightInputUpdateDto

}