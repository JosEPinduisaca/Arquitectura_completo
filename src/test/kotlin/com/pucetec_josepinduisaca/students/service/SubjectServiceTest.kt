package com.pucetec_josepinduisaca.students.service

import com.pucetec_josepinduisaca.students.dto.SubjectRequest
import com.pucetec_josepinduisaca.students.entity.Professor
import com.pucetec_josepinduisaca.students.entity.Subject
import com.pucetec_josepinduisaca.students.exceptions.BlankNameException
import com.pucetec_josepinduisaca.students.exceptions.ProfessorNotFound
import com.pucetec_josepinduisaca.students.exceptions.SubjectNotFoundException
import com.pucetec_josepinduisaca.students.repository.ProfessorRepository
import com.pucetec_josepinduisaca.students.repository.SubjectRepository
import com.pucetec_josepinduisaca.students.service.SubjectService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class SubjectServiceTest {

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var subjectService: SubjectService

    // ─────────────────────────────────────────────
    // createSubject
    // ─────────────────────────────────────────────

    @Test
    fun `createSubject retorna SubjectResponse cuando el profesor existe`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        val request = SubjectRequest(name = "Arquitectura Empresarial", code = "AE-101", professorId = 1L)
        val saved = Subject(id = 1L, name = "Arquitectura Empresarial", code = "AE-101", professor = professor)

        Mockito.`when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        Mockito.`when`(subjectRepository.save(org.mockito.kotlin.any())).thenReturn(saved)

        // Act
        val response = subjectService.createSubject(request)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("Arquitectura Empresarial", response.name)
        Assertions.assertEquals("AE-101", response.code)
        Assertions.assertEquals("Dr. García", response.professor.name)
    }

    @Test
    fun `createSubject lanza ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        val request = SubjectRequest(name = "Arquitectura Empresarial", code = "AE-101", professorId = 99L)
        Mockito.`when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            subjectService.createSubject(request)
        }
    }

    // ─────────────────────────────────────────────
    // getAllSubjects
    // ─────────────────────────────────────────────

    @Test
    fun `getAllSubjects retorna lista de SubjectResponse`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        val subjects = listOf(
            Subject(id = 1L, name = "Arquitectura Empresarial", code = "AE-101", professor = professor),
            Subject(id = 2L, name = "Bases de Datos", code = "BD-101", professor = professor)
        )
        Mockito.`when`(subjectRepository.findAll()).thenReturn(subjects)

        // Act
        val result = subjectService.getAllSubjects()

        // Assert
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals("Arquitectura Empresarial", result[0].name)
        Assertions.assertEquals("Bases de Datos", result[1].name)
    }

    @Test
    fun `getAllSubjects retorna lista vacia cuando no hay materias`() {
        // Arrange
        Mockito.`when`(subjectRepository.findAll()).thenReturn(emptyList())

        // Act
        val result = subjectService.getAllSubjects()

        // Assert
        Assertions.assertEquals(0, result.size)
    }

    // ─────────────────────────────────────────────
    // getSubjectById
    // ─────────────────────────────────────────────

    @Test
    fun `getSubjectById retorna SubjectResponse cuando la materia existe`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        val subject = Subject(id = 1L, name = "Arquitectura Empresarial", code = "AE-101", professor = professor)
        Mockito.`when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        // Act
        val response = subjectService.getSubjectById(1L)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("Arquitectura Empresarial", response.name)
    }

    @Test
    fun `getSubjectById lanza SubjectNotFoundException cuando la materia no existe`() {
        // Arrange
        Mockito.`when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFoundException> {
            subjectService.getSubjectById(99L)
        }
    }

    // ─────────────────────────────────────────────
    // updateSubject
    // ─────────────────────────────────────────────

    @Test
    fun `updateSubject retorna SubjectResponse actualizado cuando los datos son validos`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        val existing = Subject(id = 1L, name = "Arquitectura Empresarial", code = "AE-101", professor = professor)
        val request = SubjectRequest(name = "Arquitectura Empresarial II", code = "AE-102", professorId = 1L)
        val updated = Subject(id = 1L, name = "Arquitectura Empresarial II", code = "AE-102", professor = professor)

        Mockito.`when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existing))
        Mockito.`when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        Mockito.`when`(subjectRepository.save(org.mockito.kotlin.any())).thenReturn(updated)

        // Act
        val response = subjectService.updateSubject(1L, request)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("Arquitectura Empresarial II", response.name)
        Assertions.assertEquals("AE-102", response.code)
    }

    @Test
    fun `updateSubject lanza SubjectNotFoundException cuando la materia no existe`() {
        // Arrange
        val request = SubjectRequest(name = "Arquitectura Empresarial II", code = "AE-102", professorId = 1L)
        Mockito.`when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFoundException> {
            subjectService.updateSubject(99L, request)
        }
    }

    @Test
    fun `updateSubject lanza BlankNameException cuando el nombre esta en blanco`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        val existing = Subject(id = 1L, name = "Arquitectura Empresarial", code = "AE-101", professor = professor)
        val request = SubjectRequest(name = "", code = "AE-101", professorId = 1L)
        Mockito.`when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existing))

        // Act & Assert
        assertThrows<BlankNameException> {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `updateSubject lanza ProfessorNotFound cuando el nuevo profesor no existe`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        val existing = Subject(id = 1L, name = "Arquitectura Empresarial", code = "AE-101", professor = professor)
        val request = SubjectRequest(name = "Arquitectura Empresarial", code = "AE-101", professorId = 99L)

        Mockito.`when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existing))
        Mockito.`when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            subjectService.updateSubject(1L, request)
        }
    }

    // ─────────────────────────────────────────────
    // deleteSubject
    // ─────────────────────────────────────────────

    @Test
    fun `deleteSubject elimina la materia cuando existe`() {
        // Arrange
        Mockito.`when`(subjectRepository.existsById(1L)).thenReturn(true)

        // Act
        subjectService.deleteSubject(1L)

        // Assert
        Mockito.verify(subjectRepository).deleteById(1L)
    }

    @Test
    fun `deleteSubject lanza SubjectNotFoundException cuando la materia no existe`() {
        // Arrange
        Mockito.`when`(subjectRepository.existsById(99L)).thenReturn(false)

        // Act & Assert
        assertThrows<SubjectNotFoundException> {
            subjectService.deleteSubject(99L)
        }
    }
}