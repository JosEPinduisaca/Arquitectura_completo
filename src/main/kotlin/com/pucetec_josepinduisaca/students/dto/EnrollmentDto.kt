package com.pucetec_josepinduisaca.students.dto

// para crear una nueva inscripción
data class EnrollmentRequest(
    //peticion para inscripcion
    val studentId: Long,
    val subjectId: Long,
)
//para actializar datos, cambiar unicamente el estado
data class EnrollmentStatusRequest(
    val status: String,
)
// la respuesta del servidor, da detalles de una inscripcion
data class EnrollmentResponse(
    val id: Long,
    val createdAt: String,// cuando se creo
    val status: String,// el estado actual
    val subject: SubjectResponse,
    val student: StudentResponse,
)