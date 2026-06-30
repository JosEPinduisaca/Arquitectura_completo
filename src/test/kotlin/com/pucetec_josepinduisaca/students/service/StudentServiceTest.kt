package com.pucetec_josepinduisaca.students.service

import com.pucetec_josepinduisaca.students.dto.StudentRequest
import com.pucetec_josepinduisaca.students.entity.Student
import com.pucetec_josepinduisaca.students.exceptions.BlankNameException
import com.pucetec_josepinduisaca.students.exceptions.StudentNotFoundException
import com.pucetec_josepinduisaca.students.repository.StudentRepository
import com.pucetec_josepinduisaca.students.service.StudentService
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
class StudentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentService: StudentService

    // ─────────────────────────────────────────────
    // createStudent
    // ─────────────────────────────────────────────

    @Test
    fun `createStudent retorna StudentResponse cuando el nombre es valido`() {
        // Arrange
        val request = StudentRequest(name = "Ana Torres", email = "ana@puce.edu.ec")
        val saved = Student(id = 1L, name = "Ana Torres", email = "ana@puce.edu.ec")
        Mockito.`when`(studentRepository.save(org.mockito.kotlin.any())).thenReturn(saved)

        // Act
        val response = studentService.createStudent(request)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("Ana Torres", response.name)
        Assertions.assertEquals("ana@puce.edu.ec", response.email)
    }

    @Test
    fun `createStudent lanza BlankNameException cuando el nombre esta en blanco`() {
        // Arrange
        val request = StudentRequest(name = "   ", email = "ana@puce.edu.ec")

        // Act & Assert
        assertThrows<BlankNameException> {
            studentService.createStudent(request)
        }
    }

    // ─────────────────────────────────────────────
    // getAllStudents
    // ─────────────────────────────────────────────

    @Test
    fun `getAllStudents retorna lista de StudentResponse`() {
        // Arrange
        val students = listOf(
            Student(id = 1L, name = "Ana", email = "ana@puce.edu.ec"),
            Student(id = 2L, name = "Luis", email = "luis@puce.edu.ec")
        )
        Mockito.`when`(studentRepository.findAll()).thenReturn(students)

        // Act
        val result = studentService.getAllStudents()

        // Assert
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals("Ana", result[0].name)
        Assertions.assertEquals("Luis", result[1].name)
    }

    @Test
    fun `getAllStudents retorna lista vacia cuando no hay estudiantes`() {
        // Arrange
        Mockito.`when`(studentRepository.findAll()).thenReturn(emptyList())

        // Act
        val result = studentService.getAllStudents()

        // Assert
        Assertions.assertEquals(0, result.size)
    }

    // ─────────────────────────────────────────────
    // getStudentById
    // ─────────────────────────────────────────────

    @Test
    fun `getStudentById retorna StudentResponse cuando el estudiante existe`() {
        // Arrange
        val student = Student(id = 1L, name = "Ana Torres", email = "ana@puce.edu.ec")
        Mockito.`when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        // Act
        val response = studentService.getStudentById(1L)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("Ana Torres", response.name)
    }

    @Test
    fun `getStudentById lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        Mockito.`when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.getStudentById(99L)
        }
    }

    // ─────────────────────────────────────────────
    // updateStudent
    // ─────────────────────────────────────────────

    @Test
    fun `updateStudent retorna StudentResponse actualizado cuando los datos son validos`() {
        // Arrange
        val existing = Student(id = 1L, name = "Ana Torres", email = "ana@puce.edu.ec")
        val request = StudentRequest(name = "Ana Torres Ruiz", email = "ana.ruiz@puce.edu.ec")
        val updated = Student(id = 1L, name = "Ana Torres Ruiz", email = "ana.ruiz@puce.edu.ec")

        Mockito.`when`(studentRepository.findById(1L)).thenReturn(Optional.of(existing))
        Mockito.`when`(studentRepository.save(org.mockito.kotlin.any())).thenReturn(updated)

        // Act
        val response = studentService.updateStudent(1L, request)

        // Assert
        Assertions.assertEquals(1L, response.id)
        Assertions.assertEquals("Ana Torres Ruiz", response.name)
        Assertions.assertEquals("ana.ruiz@puce.edu.ec", response.email)
    }

    @Test
    fun `updateStudent lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        val request = StudentRequest(name = "Ana Torres Ruiz", email = "ana@puce.edu.ec")
        Mockito.`when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.updateStudent(99L, request)
        }
    }

    @Test
    fun `updateStudent lanza BlankNameException cuando el nombre esta en blanco`() {
        // Arrange
        val existing = Student(id = 1L, name = "Ana Torres", email = "ana@puce.edu.ec")
        val request = StudentRequest(name = "", email = "ana@puce.edu.ec")
        Mockito.`when`(studentRepository.findById(1L)).thenReturn(Optional.of(existing))

        // Act & Assert
        assertThrows<BlankNameException> {
            studentService.updateStudent(1L, request)
        }
    }

    // ─────────────────────────────────────────────
    // deleteStudent
    // ─────────────────────────────────────────────

    @Test
    fun `deleteStudent elimina el estudiante cuando existe`() {
        // Arrange
        Mockito.`when`(studentRepository.existsById(1L)).thenReturn(true)

        // Act
        studentService.deleteStudent(1L)

        // Assert
        Mockito.verify(studentRepository).deleteById(1L)
    }

    @Test
    fun `deleteStudent lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        Mockito.`when`(studentRepository.existsById(99L)).thenReturn(false)

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.deleteStudent(99L)
        }
    }
}