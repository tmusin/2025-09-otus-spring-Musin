package ru.musintimur.hw02.service

interface IOService {
    fun printLine(s: String)

    fun printFormattedLine(
        s: String,
        vararg args: Any?,
    )

    fun readString(): String

    fun readStringWithPrompt(prompt: String): String

    fun readIntFromRange(
        min: Int,
        max: Int,
        errorMessage: String,
    ): Int

    fun readIntFromRangeWithPrompt(
        min: Int,
        max: Int,
        prompt: String,
        errorMessage: String,
    ): Int
}
