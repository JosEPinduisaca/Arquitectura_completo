package com.pucetec_josepinduisaca.students.mappers

import com.pucetec_josepinduisaca.students.dto.StudentRequest
import com.pucetec_josepinduisaca.students.dto.StudentResponse
import com.pucetec_josepinduisaca.students.entity.Student


fun StudentRequest.toEntity() = Student(
    name = this.name,
    email = this.email
)

fun Student.toResponse() = StudentResponse(
    id = this.id,
    name = this.name,
    email = this.email
)