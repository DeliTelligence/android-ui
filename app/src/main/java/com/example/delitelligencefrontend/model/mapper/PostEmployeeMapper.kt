package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.type.EmployeeInputCreateDto
import com.example.delitelligence.type.EmployeeInputUpdateDto
import com.example.delitelligencefrontend.enumformodel.EmployeeTitle
import com.example.delitelligencefrontend.model.EmployeeCreate
import com.example.delitelligencefrontend.model.EmployeeUpdate
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import java.util.Optional


interface PostEmployeeMapper {

    companion object {
        val INSTANCE: PostEmployeeMapper = Mappers.getMapper(PostEmployeeMapper::class.java)
    }

    fun toEmployeeInputCreateDto(employeeCreate: EmployeeCreate): EmployeeInputCreateDto

    @Mapping(
        target = "employeeFirstName",
        source = "employeeFirstName",
        qualifiedByName = ["stringToOptional"]
    )
    @Mapping(
        target = "employeeLastName",
        source = "employeeLastName",
        qualifiedByName = ["stringToOptional"]
    )
    @Mapping(
        target = "employeeTitle",
        source = "employeeTitle",
        qualifiedByName = ["enumToOptional"]
    )
    @Mapping(
        target = "employeePassword",
        source = "employeePassword",
        qualifiedByName = ["stringToOptional"]
    )
    fun toEmployeeInputUpdateDto(employeeUpdate: EmployeeUpdate?): EmployeeInputUpdateDto?

    @Named("stringToOptional")
    fun stringToOptional(value: String?): Optional<String> {
        return Optional.ofNullable(value)
    }

    @Named("enumToOptional")
    fun enumToOptional(value: EmployeeTitle?): Optional<EmployeeTitle> {
        return Optional.ofNullable(value)
    }}