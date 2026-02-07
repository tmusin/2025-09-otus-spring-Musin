package ru.musintimur.hw12.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(name = "username", nullable = false, unique = true)
    var username: String,
    @Column(name = "password", nullable = false)
    var password: String,
    @Column(name = "enabled", nullable = false)
    var enabled: Boolean = true,
)
