package com.pucetec_josepinduisaca.students.mappers

import com.pucetec_josepinduisaca.students.dto.ProfessorRequest
import com.pucetec_josepinduisaca.students.dto.ProfessorResponse
import com.pucetec_josepinduisaca.students.entity.Professor

fun ProfessorRequest.toEntity(): Professor {
    return Professor(
        name = this.name,
        email = this.email
    )
}

fun Professor.toResponse(): ProfessorResponse {
    return ProfessorResponse(
        id = this.id,
        name = this.name,
        email = this.email
    )
}