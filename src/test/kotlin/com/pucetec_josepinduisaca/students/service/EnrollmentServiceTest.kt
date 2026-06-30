package com.pucetec_josepinduisaca.students.service

import com.pucetec_josepinduisaca.students.dto.EnrollmentRequest
import com.pucetec_josepinduisaca.students.entity.Enrollment
import com.pucetec_josepinduisaca.students.entity.Professor
import com.pucetec_josepinduisaca.students.entity.Student
import com.pucetec_josepinduisaca.students.entity.Subject
import com.pucetec_josepinduisaca.students.exceptions.EnrollmentNotFound
import com.pucetec_josepinduisaca.students.exceptions.StudentNotFoundException
import com.pucetec_josepinduisaca.students.exceptions.SubjectNotFoundException
import com.pucetec_josepinduisaca.students.repository.EnrollmentRepository
import com.pucetec_josepinduisaca.students.repository.StudentRepository
import com.pucetec_josepinduisaca.students.repository.SubjectRepository
import com.pucetec_josepinduisaca.students.service.EnrollmentService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class EnrollmentServiceTest {

    @Mock
    private lateinit var enrollmentRepository: EnrollmentRepository

    @Mock
    private lateinit var studentRepository: StudentRepository

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @InjectMocks
    private lateinit var enrollmentService: EnrollmentService

    // Helpers reutilizables
    private val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
    private val student = Student(id = 1L, name = "Ana Torres", email = "ana@puce.edu.ec")
    private val subject = Subject(id = 1L, name = "Arquitectura Empresarial", code = "AE-101", professor = professor)

    // ─────────────────────────────────────────────
    // createEnrollment
    // ─────────────────────────────────────────────

    @Test
    fun `createEnrollment retorna EnrollmentResponse cuando los datos son validos`() {
        // Arrange
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L)
        val saved = Enrollment(
            id = 1L,
            status = "INSCRITO",
            student = student,
            subject = subject,
            createdAt = LocalDateTime.now()
        )

        Mockito.`when`(enrollmentRepository.existsByStudentIdAndSubjectId(1L, 1L)).thenReturn(false)
        Mockito.`when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        Mockito.`when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))
        Mockito.`when`(enrollmentRepository.save(org.mockito.kotlin.any())).thenReturn(saved)

        // Act
        val response = enrollmentService.createEnrollment(request)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("INSCRITO", response.status)
        Assertions.assertEquals("Ana Torres", response.student.name)
        Assertions.assertEquals("Arquitectura Empresarial", response.subject.name)
    }

    @Test
    fun `createEnrollment lanza RuntimeException cuando el estudiante ya esta matriculado en la materia`() {
        // Arrange
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L)
        Mockito.`when`(enrollmentRepository.existsByStudentIdAndSubjectId(1L, 1L)).thenReturn(true)

        // Act & Assert
        assertThrows<RuntimeException> {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        val request = EnrollmentRequest(studentId = 99L, subjectId = 1L)
        Mockito.`when`(enrollmentRepository.existsByStudentIdAndSubjectId(99L, 1L)).thenReturn(false)
        Mockito.`when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment lanza SubjectNotFoundException cuando la materia no existe`() {
        // Arrange
        val request = EnrollmentRequest(studentId = 1L, subjectId = 99L)
        Mockito.`when`(enrollmentRepository.existsByStudentIdAndSubjectId(1L, 99L)).thenReturn(false)
        Mockito.`when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        Mockito.`when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFoundException> {
            enrollmentService.createEnrollment(request)
        }
    }

    // ─────────────────────────────────────────────
    // getAllEnrollments
    // ─────────────────────────────────────────────

    @Test
    fun `getAllEnrollments retorna lista de EnrollmentResponse`() {
        // Arrange
        val enrollments = listOf(
            Enrollment(
                id = 1L,
                status = "INSCRITO",
                student = student,
                subject = subject,
                createdAt = LocalDateTime.now()
            ),
            Enrollment(
                id = 2L,
                status = "APROBADO",
                student = student,
                subject = subject,
                createdAt = LocalDateTime.now()
            )
        )
        Mockito.`when`(enrollmentRepository.findAll()).thenReturn(enrollments)

        // Act
        val result = enrollmentService.getAllEnrollments()

        // Assert
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals("INSCRITO", result[0].status)
        Assertions.assertEquals("APROBADO", result[1].status)
    }

    @Test
    fun `getAllEnrollments retorna lista vacia cuando no hay matriculas`() {
        // Arrange
        Mockito.`when`(enrollmentRepository.findAll()).thenReturn(emptyList())

        // Act
        val result = enrollmentService.getAllEnrollments()

        // Assert
        Assertions.assertEquals(0, result.size)
    }

    // ─────────────────────────────────────────────
    // getEnrollmentById
    // ─────────────────────────────────────────────

    @Test
    fun `getEnrollmentById retorna EnrollmentResponse cuando la matricula existe`() {
        // Arrange
        val enrollment = Enrollment(
            id = 1L,
            status = "INSCRITO",
            student = student,
            subject = subject,
            createdAt = LocalDateTime.now()
        )
        Mockito.`when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))

        // Act
        val response = enrollmentService.getEnrollmentById(1L)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("INSCRITO", response.status)
    }

    @Test
    fun `getEnrollmentById lanza EnrollmentNotFound cuando la matricula no existe`() {
        // Arrange
        Mockito.`when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFound> {
            enrollmentService.getEnrollmentById(99L)
        }
    }

    // ─────────────────────────────────────────────
    // updateEnrollmentStatus
    // ─────────────────────────────────────────────

    @Test
    fun `updateEnrollmentStatus retorna EnrollmentResponse con el nuevo estado`() {
        // Arrange
        val now = LocalDateTime.now()
        val existing = Enrollment(id = 1L, status = "INSCRITO", student = student, subject = subject, createdAt = now)
        val updated = Enrollment(id = 1L, status = "APROBADO", student = student, subject = subject, createdAt = now)

        Mockito.`when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(existing))
        Mockito.`when`(enrollmentRepository.save(org.mockito.kotlin.any())).thenReturn(updated)

        // Act
        val response = enrollmentService.updateEnrollmentStatus(1L, "APROBADO")

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("APROBADO", response.status)
    }

    @Test
    fun `updateEnrollmentStatus lanza EnrollmentNotFound cuando la matricula no existe`() {
        // Arrange
        Mockito.`when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFound> {
            enrollmentService.updateEnrollmentStatus(99L, "APROBADO")
        }
    }

    // ─────────────────────────────────────────────
    // deleteEnrollment
    // ─────────────────────────────────────────────

    @Test
    fun `deleteEnrollment elimina la matricula cuando existe`() {
        // Arrange
        Mockito.`when`(enrollmentRepository.existsById(1L)).thenReturn(true)

        // Act
        enrollmentService.deleteEnrollment(1L)

        // Assert
        Mockito.verify(enrollmentRepository).deleteById(1L)
    }

    @Test
    fun `deleteEnrollment lanza EnrollmentNotFound cuando la matricula no existe`() {
        // Arrange
        Mockito.`when`(enrollmentRepository.existsById(99L)).thenReturn(false)

        // Act & Assert
        assertThrows<EnrollmentNotFound> {
            enrollmentService.deleteEnrollment(99L)
        }
    }
}