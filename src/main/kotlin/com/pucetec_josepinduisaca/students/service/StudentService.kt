package com.pucetec_josepinduisaca.students.service

import com.pucetec_josepinduisaca.students.dto.StudentRequest
import com.pucetec_josepinduisaca.students.dto.StudentResponse
import com.pucetec_josepinduisaca.students.exceptions.BlankNameException
import com.pucetec_josepinduisaca.students.exceptions.StudentNotFoundException
import com.pucetec_josepinduisaca.students.entity.Student
import com.pucetec_josepinduisaca.students.mappers.toResponse
import com.pucetec_josepinduisaca.students.repository.StudentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentRepository: StudentRepository
) {
    private val logger = LoggerFactory.getLogger(StudentService::class.java)

    fun createStudent(request: StudentRequest): StudentResponse {
        logger.info("Creating student: ${request.name}")

        if (request.name.isBlank()) {
            throw BlankNameException("El nombre del estudiante no puede estar vacío")
        }

        val studentToSave = Student(
            name = request.name,
            email = request.email
        )
        val savedStudent = studentRepository.save(studentToSave)
        logger.info("Save student with id ${savedStudent.id}")

        return savedStudent.toResponse()
    }

    fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")
        val students = studentRepository.findAll()
        return students.map { it.toResponse() }
    }

    fun getStudentById(id: Long): StudentResponse {
        logger.info("Getting student with id: $id")
        val student = studentRepository.findById(id)
            .orElseThrow { StudentNotFoundException("Estudiante no encontrado con el ID: $id") }
        return student.toResponse()
    }

    fun updateStudent(id: Long, request: StudentRequest): StudentResponse {
        logger.info("Updating student with id: $id")

        val existingStudent = studentRepository.findById(id)
            .orElseThrow { StudentNotFoundException("Estudiante no encontrado con el ID: $id") }

        if (request.name.isBlank()) {
            throw BlankNameException("El nombre del estudiante no puede estar vacío")
        }

        val studentToUpdate = Student(
            id = existingStudent.id,
            name = request.name,
            email = request.email
        )

        val updatedStudent = studentRepository.save(studentToUpdate)
        logger.info("Updated student with id ${updatedStudent.id}")

        return updatedStudent.toResponse()
    }

    fun deleteStudent(id: Long) {
        logger.info("Deleting student with id: $id")

        if (!studentRepository.existsById(id)) {
            throw StudentNotFoundException("No se puede eliminar. Estudiante no encontrado con el ID: $id")
        }

        studentRepository.deleteById(id)
        logger.info("Deleted student with id: $id")
    }
}