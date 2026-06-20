package com.pucetec_josepinduisaca.students.repository
import com.pucetec_josepinduisaca.students.entity.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepository : JpaRepository<Subject, Long>{
    fun existsByCode(code: String): Boolean
}