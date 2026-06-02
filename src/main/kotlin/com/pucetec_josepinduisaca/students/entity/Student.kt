package com.pucetec_josepinduisaca.students.entity

import jakarta.persistence.*

@Entity
@Table(name = "students")
class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String
)