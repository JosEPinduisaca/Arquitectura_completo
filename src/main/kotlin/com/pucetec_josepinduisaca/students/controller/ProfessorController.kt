package com.pucetec_josepinduisaca.students.controller

import com.pucetec_josepinduisaca.students.dto.ProfessorRequest
import com.pucetec_josepinduisaca.students.dto.ProfessorResponse
import com.pucetec_josepinduisaca.students.service.ProfessorService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProfessorController(
    val professorService: ProfessorService
) {
    private val logger = LoggerFactory.getLogger(ProfessorController::class.java)

    @PostMapping("/api/professors")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfessor(
        @RequestBody request: ProfessorRequest
    ): ProfessorResponse {
        logger.info("Creating Professor ${request.name}")
        return professorService.createProfessor(request)
    }

    @GetMapping("/api/professors")
    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Getting all professors")
        return professorService.getAllProfessors()
    }

    @GetMapping("/api/professors/{id}")
    fun getProfessorById(
        @PathVariable id: Long
    ): ProfessorResponse {
        logger.info("Getting professor with id: $id")
        return professorService.getProfessorById(id)
    }

    @PutMapping("/api/professors/{id}")
    fun updateProfessor(
        @PathVariable id: Long,
        @RequestBody request: ProfessorRequest
    ): ResponseEntity<ProfessorResponse> {
        logger.info("Updating professor with id: $id")
        val response = professorService.updateProfessor(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/api/professors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProfessor(
        @PathVariable id: Long
    ) {
        logger.info("Deleting professor with id: $id")
        professorService.deleteProfessor(id)
    }
}