package com.pucetec_josepinduisaca.students.repository


import com.pucetec_josepinduisaca.students.entity.Professor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfessorRepository : JpaRepository<Professor, Long>{
    fun existsByEmail(email: String): Boolean
}