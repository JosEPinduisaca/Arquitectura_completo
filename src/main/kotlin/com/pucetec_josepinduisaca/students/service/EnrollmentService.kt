package com.pucetec_josepinduisaca.students.service

import com.pucetec_josepinduisaca.students.dto.EnrollmentRequest
import com.pucetec_josepinduisaca.students.dto.EnrollmentResponse
import com.pucetec_josepinduisaca.students.entity.Enrollment
import com.pucetec_josepinduisaca.students.exceptions.StudentNotFoundException
import com.pucetec_josepinduisaca.students.exceptions.SubjectNotFoundException
import com.pucetec_josepinduisaca.students.mappers.toResponse
import com.pucetec_josepinduisaca.students.repository.EnrollmentRepository
import com.pucetec_josepinduisaca.students.repository.StudentRepository
import com.pucetec_josepinduisaca.students.repository.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentRepository: StudentRepository,
    private val subjectRepository: SubjectRepository
) {
    private val logger = LoggerFactory.getLogger(EnrollmentService::class.java)

    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        logger.info("Enrolling student ${request.studentId} into subject ${request.subjectId}")

        if (enrollmentRepository.existsByStudentIdAndSubjectId(request.studentId, request.subjectId)) {
            throw RuntimeException("El estudiante ya está matriculado en esta materia")
        }

        val student = studentRepository.findById(request.studentId)
            .orElseThrow { StudentNotFoundException("Estudiante no encontrado con ID: ${request.studentId}") }

        val subject = subjectRepository.findById(request.subjectId)
            .orElseThrow { SubjectNotFoundException("Materia no encontrada con ID: ${request.subjectId}") }

        val enrollmentToSave = Enrollment(
            status = "ACTIVE",
            student = student,
            subject = subject
        )

        val savedEnrollment = enrollmentRepository.save(enrollmentToSave)
        logger.info("Enrollment saved with id ${savedEnrollment.id}")
        return savedEnrollment.toResponse()
    }

    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Getting all enrollments")
        return enrollmentRepository.findAll().map { it.toResponse() }
    }

    fun getEnrollmentById(id: Long): EnrollmentResponse {
        logger.info("Getting enrollment with id: $id")
        val enrollment = enrollmentRepository.findById(id)
            .orElseThrow { RuntimeException("Matrícula no encontrada con ID: $id") }
        return enrollment.toResponse()
    }

    fun updateEnrollmentStatus(id: Long, status: String): EnrollmentResponse {
        logger.info("Updating status of enrollment with id: $id to $status")
        val enrollment = enrollmentRepository.findById(id)
            .orElseThrow { RuntimeException("Matrícula no encontrada con ID: $id") }

        val updatedEnrollment = Enrollment(
            id = enrollment.id,
            createdAt = enrollment.createdAt,
            status = status,
            student = enrollment.student,
            subject = enrollment.subject
        )

        return enrollmentRepository.save(updatedEnrollment).toResponse()
    }

    fun deleteEnrollment(id: Long) {
        logger.info("Deleting enrollment with id: $id")
        if (!enrollmentRepository.existsById(id)) {
            throw RuntimeException("Matrícula no encontrada con ID: $id")
        }
        enrollmentRepository.deleteById(id)
    }
}