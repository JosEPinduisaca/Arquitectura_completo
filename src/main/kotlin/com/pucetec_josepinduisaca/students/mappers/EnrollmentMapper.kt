package com.pucetec_josepinduisaca.students.mappers

import com.pucetec_josepinduisaca.students.dto.EnrollmentResponse
import com.pucetec_josepinduisaca.students.entity.Enrollment


fun Enrollment.toResponse() = EnrollmentResponse(
    id = this.id,
    createdAt = this.createdAt.toString(),
    status = this.status,
    subject = this.subject.toResponse(),
    student = this.student.toResponse()
)