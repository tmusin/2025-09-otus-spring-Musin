package ru.musintimur.hw01.service

interface IOService {
    fun printLine(s: String)

    fun printFormatLine(
        s: String,
        vararg args: Any?,
    )
}
