package ru.musintimur.hw04.domain

data class Student(
    val firstName: String,
    val lastName: String,
) {
    fun getFullName() = "$firstName $lastName"
}
