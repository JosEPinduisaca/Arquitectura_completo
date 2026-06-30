package com.pucetec_josepinduisaca.students.dto
// Petición para crear y editar
data class ProfessorRequest(
    val name: String,
    val email: String,
    //la base de datos lo genera automáticamente
)
// envia informacion del profesor, el servidor
data class ProfessorResponse(
    val id: Long,
    val name: String,
    val email: String,
)