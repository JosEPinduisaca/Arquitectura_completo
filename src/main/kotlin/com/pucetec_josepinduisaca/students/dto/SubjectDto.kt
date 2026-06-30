package com.pucetec_josepinduisaca.students.dto

//crear una nueva materia o modificar una existente
data class SubjectRequest(
    val name: String,
    val code: String,
    val professorId: Long,//verificar si el profesor existe
)
//sale lo consultado de la materia junto con la informacion del profesor
data class SubjectResponse(
    val id: Long,
    val name: String,
    val code: String,
    val professor: ProfessorResponse,//saber que profesor dicta la materia
)