package com.pucetec_josepinduisaca.students.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.transaction.Status
import java.time.LocalDateTime

@Entity
@Table(name = "enrollments")
class Enrollment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,


    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val status: String = "",
////.......
    @ManyToOne(fetch = FetchType.LAZY)
    val subject: Subject, //materias
///// conexcion, las claves foraneas y genera subject_id
    @ManyToOne(fetch = FetchType.LAZY)
    val student: Student,// estudiantes
//// conexcion, las claves foraneas y genera  student_id


    )