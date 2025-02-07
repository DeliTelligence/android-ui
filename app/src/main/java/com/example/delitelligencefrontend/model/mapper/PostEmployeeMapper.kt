package com.example.delitelligencefrontend.model.mapper

import com.example.delitelligence.type.EmployeeInputCreateDto
import com.example.delitelligence.type.EmployeeInputUpdateDto
import com.example.delitelligencefrontend.enumformodel.EmployeeTitle
import com.example.delitelligencefrontend.model.EmployeeCreate
import com.example.delitelligencefrontend.model.EmployeeFetch
import com.example.delitelligencefrontend.model.EmployeeUpdate
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import java.util.Optional

@Mapper(componentModel = "default")
interface PostEmployeeMapper {

    companion object {
        val INSTANCE: PostEmployeeMapper = Mappers.getMapper(PostEmployeeMapper::class.java)
    }

    fun toEmployeeInputCreateDto(employeeCreate: EmployeeCreate): EmployeeInputCreateDto

    fun toEmployeeInputUpdateDto(employeeUpdate: EmployeeUpdate): EmployeeInputUpdateDto

}
