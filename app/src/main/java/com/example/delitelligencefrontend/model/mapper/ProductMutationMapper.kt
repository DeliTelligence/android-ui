package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.type.ProductCreateDto
import com.example.delitelligence.type.ProductUpdateDto
import com.example.delitelligencefrontend.modeldto.product.ProductCreate
import com.example.delitelligencefrontend.modeldto.product.ProductUpdate
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers


@Mapper(componentModel = "default", uses = [StandardWeightProductMapper::class])

interface ProductMutationMapper {

    companion object {
        val INSTANCE: ProductMutationMapper = Mappers.getMapper(ProductMutationMapper::class.java)
    }

    @Mapping(target = "productType", source = "productType")
    @BeanMapping(ignoreByDefault = true)
    fun toProductCreateDto(productCreate: ProductCreate): ProductCreateDto

    @Mapping(target = "productType", source = "productType")
    @BeanMapping(ignoreByDefault = true)
    fun toProductUpdateDto(productUpdate: ProductUpdate): ProductUpdateDto
}