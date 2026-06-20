package com.pucetec_josepinduisaca.students.controller

import com.pucetec_josepinduisaca.students.dto.SubjectRequest
import com.pucetec_josepinduisaca.students.dto.SubjectResponse
import com.pucetec_josepinduisaca.students.service.SubjectService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SubjectController(
    private val subjectService: SubjectService
) {
    private val logger = LoggerFactory.getLogger(SubjectController::class.java)

    @PostMapping("/api/subjects")
    @ResponseStatus(HttpStatus.CREATED)
    fun createSubject(
        @RequestBody request: SubjectRequest
    ): SubjectResponse {
        logger.info("Creating Subject ${request.name}")
        return subjectService.createSubject(request)
    }

    @GetMapping("/api/subjects")
    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Getting all subjects")
        return subjectService.getAllSubjects()
    }

    @GetMapping("/api/subjects/{id}")
    fun getSubjectById(
        @PathVariable id: Long
    ): SubjectResponse {
        logger.info("Getting subject with id: $id")
        return subjectService.getSubjectById(id)
    }

    @PutMapping("/api/subjects/{id}")
    fun updateSubject(
        @PathVariable id: Long,
        @RequestBody request: SubjectRequest
    ): ResponseEntity<SubjectResponse> {
        logger.info("Updating subject with id: $id")
        val response = subjectService.updateSubject(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/api/subjects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSubject(
        @PathVariable id: Long
    ) {
        logger.info("Deleting subject with id: $id")
        subjectService.deleteSubject(id)
    }
}