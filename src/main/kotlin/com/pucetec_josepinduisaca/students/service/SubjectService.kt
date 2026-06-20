package com.pucetec_josepinduisaca.students.service


import com.pucetec_josepinduisaca.students.dto.SubjectRequest
import com.pucetec_josepinduisaca.students.dto.SubjectResponse
import com.pucetec_josepinduisaca.students.entity.Subject
import com.pucetec_josepinduisaca.students.exceptions.BlankNameException
import com.pucetec_josepinduisaca.students.exceptions.SubjectNotFoundException
import com.pucetec_josepinduisaca.students.exceptions.ProfessorNotFound
import com.pucetec_josepinduisaca.students.mappers.toResponse
import com.pucetec_josepinduisaca.students.repository.ProfessorRepository
import com.pucetec_josepinduisaca.students.repository.SubjectRepository
import org.springframework.stereotype.Service

@Service
class SubjectService (
    private val professorRepository: ProfessorRepository,
    private val subjectRepository: SubjectRepository
) {
    fun createSubject(
        request: SubjectRequest
    ): SubjectResponse {
        val professor =professorRepository.findById(request.professorId).orElseThrow {
            ProfessorNotFound("Profesor no encontrado: ${request.professorId}")
        }

        val subjectEntity = Subject(
            name = request.name,
            code = request.code,
            professor = professor
        )

        val savedSubject = subjectRepository.save(subjectEntity)
        return savedSubject.toResponse()
    }

    fun getAllSubjects(): List<SubjectResponse> {
        val saveSubjects = subjectRepository.findAll()
        return saveSubjects.map { it.toResponse() }
    }

    fun getSubjectById(id: Long): SubjectResponse {
        val subject = subjectRepository.findById(id)
            .orElseThrow { SubjectNotFoundException("Materia no encontrada con el ID: $id") }
        return subject.toResponse()
    }

    fun updateSubject(id: Long, request: SubjectRequest): SubjectResponse {
        val existingSubject = subjectRepository.findById(id)
            .orElseThrow { SubjectNotFoundException("Materia no encontrada con el ID: $id") }

        if (request.name.isBlank()) {
            throw BlankNameException("El nombre de la materia no puede estar vacío")
        }

        val professor = professorRepository.findById(request.professorId)
            .orElseThrow { ProfessorNotFound("Profesor no encontrado: ${request.professorId}") }

        val subjectToUpdate = Subject(
            id = existingSubject.id,
            name = request.name,
            code = request.code,
            professor = professor,
            enrollments = existingSubject.enrollments
        )

        val updatedSubject = subjectRepository.save(subjectToUpdate)
        return updatedSubject.toResponse()
    }

    fun deleteSubject(id: Long) {
        if (!subjectRepository.existsById(id)) {
            throw SubjectNotFoundException("No se puede eliminar. Materia no encontrada con el ID: $id")
        }
        subjectRepository.deleteById(id)
    }
}