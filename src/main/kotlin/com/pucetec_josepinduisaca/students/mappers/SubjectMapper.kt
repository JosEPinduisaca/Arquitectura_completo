package com.pucetec_josepinduisaca.students.mappers

import com.pucetec_josepinduisaca.students.dto.SubjectResponse
import com.pucetec_josepinduisaca.students.entity.Subject


fun Subject.toResponse() = SubjectResponse(
    id = this.id,
    name = this.name,
    code = this.code,
    professor = this.professor.toResponse()
)