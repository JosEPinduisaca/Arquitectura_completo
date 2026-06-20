package com.pucetec_josepinduisaca.students.repository

import com.pucetec_josepinduisaca.students.entity.Enrollment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EnrollmentRepository : JpaRepository<Enrollment, Long> {
    fun existsByStudentIdAndSubjectId(studentId: Long, subjectId: Long): Boolean
}