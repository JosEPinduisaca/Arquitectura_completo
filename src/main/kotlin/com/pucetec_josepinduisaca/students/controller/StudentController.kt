package com.pucetec_josepinduisaca.students.controller

import com.pucetec_josepinduisaca.students.dto.StudentRequest
import com.pucetec_josepinduisaca.students.dto.StudentResponse
import com.pucetec_josepinduisaca.students.service.StudentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/students")
class StudentController(private val studentService: StudentService) {
//creare los estudiantes
    @PostMapping
    fun createStudent(@RequestBody request: StudentRequest): ResponseEntity<StudentResponse> {
        val response = studentService.createStudent(request)
        return ResponseEntity(response, HttpStatus.CREATED)
    }
//creare la lista
    @GetMapping
    fun getAllStudents(): ResponseEntity<List<StudentResponse>> {
        val response = studentService.getAllStudents()
        return ResponseEntity.ok(response)
    }
}