package ru.musintimur.hw03.service

interface LocalizedMessagesService {
    fun getMessage(
        code: String,
        vararg args: Any,
    ): String
}
