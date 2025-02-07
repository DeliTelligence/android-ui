package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.type.StandardWeightProductInputCreateDto
import com.example.delitelligence.type.StandardWeightProductInputUpdateDto
import com.example.delitelligencefrontend.modeldto.product.StandardWeightProductCreate
import com.example.delitelligencefrontend.modeldto.product.StandardWeightProductUpdate
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "default", uses = [StandardWeightMapper::class])
interface StandardWeightProductMapper {

    companion object {
        val INSTANCE: StandardWeightProductMapper = Mappers.getMapper(StandardWeightProductMapper::class.java)
    }

    fun toStandardWeightProductCreateDto(standardWeightProductCreate: StandardWeightProductCreate): StandardWeightProductInputCreateDto

    fun toStandardWeightProductInputDto(standardWeightProductUpdate: StandardWeightProductUpdate): StandardWeightProductInputUpdateDto
}