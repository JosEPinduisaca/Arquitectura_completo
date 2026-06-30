package com.pucetec_josepinduisaca.students.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table


@Entity
@Table(name = "subjects")
class Subject (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    val name: String = "",

    val code: String = "",

    @ManyToOne(fetch = FetchType.LAZY)// FetchType.LAZY para optimizar el rendimiento
    // crea la clave foranea (profesor_id) en la tabla subjects
    val professor: Professor,

    @OneToMany(mappedBy = "subject",
        cascade = [CascadeType.ALL],
        orphanRemoval = true)

    val enrollments: MutableList<Enrollment> = mutableListOf(),
)