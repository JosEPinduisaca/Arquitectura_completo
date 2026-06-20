package com.pucetec_josepinduisaca.students.service

import com.pucetec_josepinduisaca.students.dto.ProfessorRequest
import com.pucetec_josepinduisaca.students.dto.ProfessorResponse
import com.pucetec_josepinduisaca.students.exceptions.BlankNameException
import com.pucetec_josepinduisaca.students.exceptions.ProfessorNotFound
import com.pucetec_josepinduisaca.students.entity.Professor
import com.pucetec_josepinduisaca.students.mappers.toEntity
import com.pucetec_josepinduisaca.students.mappers.toResponse
import com.pucetec_josepinduisaca.students.repository.ProfessorRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val professorRepository: ProfessorRepository
) {
    private val logger = LoggerFactory.getLogger(ProfessorService::class.java)

    fun createProfessor(request: ProfessorRequest): ProfessorResponse {
        logger.info("Creating Professor ${request.name}")

        if (request.name.isBlank()) {
            throw BlankNameException("El nombre del profesor no puede estar vacío")
        }

        val emailExists = professorRepository.existsByEmail(request.email)
        if (emailExists) {
            throw RuntimeException("Email already exists")
        }

        val professorToSave = request.toEntity()
        val savedProfessor = professorRepository.save(professorToSave)
        logger.info("Save professor with id ${savedProfessor.id}")

        return savedProfessor.toResponse()
    }

    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Getting all professors")
        val professors = professorRepository.findAll()
        return professors.map { it.toResponse() }
    }

    fun getProfessorById(id: Long): ProfessorResponse {
        logger.info("Getting professor with id: $id")
        val professor = professorRepository.findById(id)
            .orElseThrow { ProfessorNotFound("Profesor no encontrado con el ID: $id") }
        return professor.toResponse()
    }

    fun updateProfessor(id: Long, request: ProfessorRequest): ProfessorResponse {
        logger.info("Updating professor with id: $id")

        val existingProfessor = professorRepository.findById(id)
            .orElseThrow { ProfessorNotFound("Profesor no encontrado con el ID: $id") }

        if (request.name.isBlank()) {
            throw BlankNameException("El nombre del profesor no puede estar vacío")
        }

        // Corrección de inmutabilidad: se instancia un nuevo objeto con los datos actualizados manteniendo el ID original
        val professorToUpdate = Professor(
            id = existingProfessor.id,
            name = request.name,
            email = request.email,
            subjects = existingProfessor.subjects
        )

        val updatedProfessor = professorRepository.save(professorToUpdate)
        logger.info("Updated professor with id ${updatedProfessor.id}")

        return updatedProfessor.toResponse()
    }

    fun deleteProfessor(id: Long) {
        logger.info("Deleting professor with id: $id")

        if (!professorRepository.existsById(id)) {
            throw ProfessorNotFound("No se puede eliminar. Profesor no encontrado con el ID: $id")
        }

        professorRepository.deleteById(id)
        logger.info("Deleted professor with id: $id")
    }
}