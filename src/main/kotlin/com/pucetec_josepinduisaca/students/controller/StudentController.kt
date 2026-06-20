package com.pucetec_josepinduisaca.students.controller

import com.pucetec_josepinduisaca.students.dto.StudentRequest
import com.pucetec_josepinduisaca.students.dto.StudentResponse
import com.pucetec_josepinduisaca.students.service.StudentService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class StudentController(
    private val studentService: StudentService
) {
    private val logger = LoggerFactory.getLogger(StudentController::class.java)

    @PostMapping("/api/students")
    @ResponseStatus(HttpStatus.CREATED)
    fun createStudent(
        @RequestBody request: StudentRequest
    ): StudentResponse {
        logger.info("Creating student: ${request.name}")
        return studentService.createStudent(request)
    }

    @GetMapping("/api/students")
    fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")
        return studentService.getAllStudents()
    }

    @GetMapping("/api/students/{id}")
    fun getStudentById(
        @PathVariable id: Long
    ): StudentResponse {
        logger.info("Getting student with id: $id")
        return studentService.getStudentById(id)
    }

    @PutMapping("/api/students/{id}")
    fun updateStudent(
        @PathVariable id: Long,
        @RequestBody request: StudentRequest
    ): ResponseEntity<StudentResponse> {
        logger.info("Updating student with id: $id")
        val response = studentService.updateStudent(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/api/students/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudent(
        @PathVariable id: Long
    ) {
        logger.info("Deleting student with id: $id")
        studentService.deleteStudent(id)
    }
}