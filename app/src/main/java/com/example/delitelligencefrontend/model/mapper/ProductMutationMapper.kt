package com.example.delitelligencefrontend.model.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.delitelligence.type.ProductCreateDto
import com.example.delitelligence.type.ProductUpdateDto
import com.example.delitelligencefrontend.modeldto.product.ProductCreate
import com.example.delitelligencefrontend.modeldto.product.ProductUpdate
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import java.io.ByteArrayOutputStream


@Mapper(componentModel = "default", uses = [StandardWeightProductMapper::class])
abstract class ProductMutationMapper {

    companion object {
        val INSTANCE: ProductMutationMapper = Mappers.getMapper(ProductMutationMapper::class.java)
    }

    @Mapping(target = "productType", source = "productType")
    @Mapping(target = "standardWeightProducts", source = "standardWeightProducts")
    @Mapping(target = "productImageDto", source = "productImageDto", qualifiedByName = ["convertImage"])
    abstract fun toProductCreateDto(productCreate: ProductCreate): ProductCreateDto

    @Mapping(target = "productType", source = "productType")
    @Mapping(target = "standardWeightProducts", source = "standardWeightProducts")
    @Mapping(target = "productImageDto", source = "productImageDto", qualifiedByName = ["convertImage"])
    abstract fun toProductUpdateDto(productUpdate: ProductUpdate): ProductUpdateDto

    @Named("convertImage")
    protected fun convertImage(imageString: String?): String? {
        if (imageString.isNullOrEmpty()) return null

        return when {
            imageString.startsWith("data:image") -> {
                val commaIndex = imageString.indexOf(',')
                if (commaIndex > 0) {
                    imageString.substring(commaIndex + 1)
                } else {
                    imageString
                }
            }
            else -> imageString // Assume it's already a raw base64 string
        }
    }
}