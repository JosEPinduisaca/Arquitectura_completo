package com.pucetec_josepinduisaca.students.service

import com.pucetec_josepinduisaca.students.dto.ProfessorRequest
import com.pucetec_josepinduisaca.students.entity.Professor
import com.pucetec_josepinduisaca.students.exceptions.BlankNameException
import com.pucetec_josepinduisaca.students.exceptions.ProfessorNotFound
import com.pucetec_josepinduisaca.students.repository.ProfessorRepository
import com.pucetec_josepinduisaca.students.service.ProfessorService
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
class ProfessorServiceTest {

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var professorService: ProfessorService

    // ─────────────────────────────────────────────
    // createProfessor
    // ─────────────────────────────────────────────

    @Test
    fun `createProfessor retorna ProfessorResponse cuando los datos son validos`() {
        // Arrange
        val request = ProfessorRequest(name = "Dr. García", email = "garcia@puce.edu.ec")
        val saved = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        Mockito.`when`(professorRepository.existsByEmail(request.email)).thenReturn(false)
        Mockito.`when`(professorRepository.save(org.mockito.kotlin.any())).thenReturn(saved)

        // Act
        val response = professorService.createProfessor(request)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("Dr. García", response.name)
        Assertions.assertEquals("garcia@puce.edu.ec", response.email)
    }

    @Test
    fun `createProfessor lanza BlankNameException cuando el nombre esta en blanco`() {
        // Arrange
        val request = ProfessorRequest(name = "   ", email = "garcia@puce.edu.ec")

        // Act & Assert
        assertThrows<BlankNameException> {
            professorService.createProfessor(request)
        }
    }

    @Test
    fun `createProfessor lanza RuntimeException cuando el email ya existe`() {
        // Arrange
        val request = ProfessorRequest(name = "Dr. García", email = "garcia@puce.edu.ec")
        Mockito.`when`(professorRepository.existsByEmail(request.email)).thenReturn(true)

        // Act & Assert
        assertThrows<RuntimeException> {
            professorService.createProfessor(request)
        }
    }

    // ─────────────────────────────────────────────
    // getAllProfessors
    // ─────────────────────────────────────────────

    @Test
    fun `getAllProfessors retorna lista de ProfessorResponse`() {
        // Arrange
        val professors = listOf(
            Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec"),
            Professor(id = 2L, name = "Dra. López", email = "lopez@puce.edu.ec")
        )
        Mockito.`when`(professorRepository.findAll()).thenReturn(professors)

        // Act
        val result = professorService.getAllProfessors()

        // Assert
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals("Dr. García", result[0].name)
        Assertions.assertEquals("Dra. López", result[1].name)
    }

    @Test
    fun `getAllProfessors retorna lista vacia cuando no hay profesores`() {
        // Arrange
        Mockito.`when`(professorRepository.findAll()).thenReturn(emptyList())

        // Act
        val result = professorService.getAllProfessors()

        // Assert
        Assertions.assertEquals(0, result.size)
    }

    // ─────────────────────────────────────────────
    // getProfessorById
    // ─────────────────────────────────────────────

    @Test
    fun `getProfessorById retorna ProfessorResponse cuando el profesor existe`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        Mockito.`when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))

        // Act
        val response = professorService.getProfessorById(1L)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("Dr. García", response.name)
    }

    @Test
    fun `getProfessorById lanza ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        Mockito.`when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            professorService.getProfessorById(99L)
        }
    }

    // ─────────────────────────────────────────────
    // updateProfessor
    // ─────────────────────────────────────────────

    @Test
    fun `updateProfessor retorna ProfessorResponse actualizado cuando los datos son validos`() {
        // Arrange
        val existing = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        val request = ProfessorRequest(name = "Dr. García Pérez", email = "garcia.perez@puce.edu.ec")
        val updated = Professor(id = 1L, name = "Dr. García Pérez", email = "garcia.perez@puce.edu.ec")

        Mockito.`when`(professorRepository.findById(1L)).thenReturn(Optional.of(existing))
        Mockito.`when`(professorRepository.save(org.mockito.kotlin.any())).thenReturn(updated)

        // Act
        val response = professorService.updateProfessor(1L, request)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("Dr. García Pérez", response.name)
        Assertions.assertEquals("garcia.perez@puce.edu.ec", response.email)
    }

    @Test
    fun `updateProfessor lanza ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        val request = ProfessorRequest(name = "Dr. García Pérez", email = "garcia@puce.edu.ec")
        Mockito.`when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            professorService.updateProfessor(99L, request)
        }
    }

    @Test
    fun `updateProfessor lanza BlankNameException cuando el nombre esta en blanco`() {
        // Arrange
        val existing = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        val request = ProfessorRequest(name = "", email = "garcia@puce.edu.ec")
        Mockito.`when`(professorRepository.findById(1L)).thenReturn(Optional.of(existing))

        // Act & Assert
        assertThrows<BlankNameException> {
            professorService.updateProfessor(1L, request)
        }
    }

    // ─────────────────────────────────────────────
    // deleteProfessor
    // ─────────────────────────────────────────────

    @Test
    fun `deleteProfessor elimina el profesor cuando existe`() {
        // Arrange
        Mockito.`when`(professorRepository.existsById(1L)).thenReturn(true)

        // Act
        professorService.deleteProfessor(1L)

        // Assert
        Mockito.verify(professorRepository).deleteById(1L)
    }

    @Test
    fun `deleteProfessor lanza ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        Mockito.`when`(professorRepository.existsById(99L)).thenReturn(false)

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            professorService.deleteProfessor(99L)
        }
    }
}