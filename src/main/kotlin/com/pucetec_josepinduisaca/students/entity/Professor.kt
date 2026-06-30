package com.pucetec_josepinduisaca.students.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "professors")
class Professor (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String = "",

    val email: String = "",

    @OneToMany(mappedBy = "professor",
        cascade = [CascadeType.ALL],
        orphanRemoval = true)
    val subjects: MutableList<Subject> = mutableListOf(),

    )