package com.pucetec_josepinduisaca.students.controller

import com.pucetec_josepinduisaca.students.dto.EnrollmentRequest
import com.pucetec_josepinduisaca.students.dto.EnrollmentResponse
import com.pucetec_josepinduisaca.students.dto.EnrollmentStatusRequest
import com.pucetec_josepinduisaca.students.service.EnrollmentService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class EnrollmentController(
    val enrollmentService: EnrollmentService
) {
    private val logger = LoggerFactory.getLogger(EnrollmentController::class.java)

    @PostMapping("/api/enrollments")
    @ResponseStatus(HttpStatus.CREATED)
    fun createEnrollment(
        @RequestBody request: EnrollmentRequest
    ): EnrollmentResponse {
        logger.info("Creating Enrollment")
        return enrollmentService.createEnrollment(request)
    }

    @GetMapping("/api/enrollments")
    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Getting all enrollments")
        return enrollmentService.getAllEnrollments()
    }

    @GetMapping("/api/enrollments/{id}")
    fun getEnrollmentById(
        @PathVariable id: Long
    ): EnrollmentResponse {
        logger.info("Getting enrollment with id: $id")
        return enrollmentService.getEnrollmentById(id)
    }

    @PutMapping("/api/enrollments/{id}")
    fun updateEnrollmentStatus(
        @PathVariable id: Long,
        @RequestBody request: EnrollmentStatusRequest
    ): ResponseEntity<EnrollmentResponse> {
        logger.info("Updating enrollment status with id: $id")
        val response = enrollmentService.updateEnrollmentStatus(id, request.status)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/api/enrollments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEnrollment(
        @PathVariable id: Long
    ) {
        logger.info("Deleting enrollment with id: $id")
        enrollmentService.deleteEnrollment(id)
    }
}