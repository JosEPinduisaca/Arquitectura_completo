package com.pucetec_josepinduisaca.students.service

import com.pucetec_josepinduisaca.students.dto.StudentRequest
import com.pucetec_josepinduisaca.students.dto.StudentResponse
import com.pucetec_josepinduisaca.students.entity.Student
import com.pucetec_josepinduisaca.students.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(private val studentRepository: StudentRepository) {

    // Lógica para crear un estudiante
    fun createStudent(request: StudentRequest): StudentResponse {
        val studentEntity = Student(
            name = request.name,
            email = request.email
        )
        val savedStudent = studentRepository.save(studentEntity)

        return StudentResponse(
            id = savedStudent.id!!,
            name = savedStudent.name,
            email = savedStudent.email
        )
    }

    // Lógica para listar todos los estudiantes
    fun getAllStudents(): List<StudentResponse> {
        val students = studentRepository.findAll()
        return students.map { student ->
            StudentResponse(
                id = student.id!!,
                name = student.name,
                email = student.email
            )
        }
    }
}