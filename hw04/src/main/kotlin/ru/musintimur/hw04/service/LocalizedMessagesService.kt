package ru.musintimur.hw04.service

interface LocalizedMessagesService {
    fun getMessage(
        code: String,
        vararg args: Any,
    ): String
}
