package com.example.demo.controller

import com.pucetec_josepinduisaca.students.dto.StudentRequest
import com.pucetec_josepinduisaca.students.dto.StudentResponse
import com.example.demo.service.StudentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/students")
class StudentController(private val studentService: StudentService) {

    // 1. Crear un estudiante (POST /students)
    @PostMapping
    fun createStudent(@RequestBody request: StudentRequest): ResponseEntity<StudentResponse> {
        val response = studentService.createStudent(request)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    // 2. Listar todos los estudiantes (GET /students)
    @GetMapping
    fun getAllStudents(): ResponseEntity<List<StudentResponse>> {
        val response = studentService.getAllStudents()
        return ResponseEntity.ok(response)
    }
}