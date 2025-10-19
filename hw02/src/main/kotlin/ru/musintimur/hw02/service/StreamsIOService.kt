package ru.musintimur.hw02.service

import org.springframework.stereotype.Service
import java.io.InputStream
import java.io.PrintStream
import java.util.Scanner

@Service
class StreamsIOService(
    private val printStream: PrintStream,
    inputStream: InputStream,
) : IOService {
    companion object {
        private const val MAX_ATTEMPTS = 10
    }

    private val scanner = Scanner(inputStream)

    override fun printLine(s: String) {
        printStream.println(s)
    }

    override fun printFormattedLine(
        s: String,
        vararg args: Any?,
    ) {
        printStream.printf(s, *args)
    }

    override fun readString(): String = scanner.nextLine()

    override fun readStringWithPrompt(prompt: String): String {
        printLine(prompt)
        return scanner.nextLine()
    }

    override fun readIntFromRange(
        min: Int,
        max: Int,
        errorMessage: String,
    ): Int {
        repeat(MAX_ATTEMPTS) {
            try {
                val stringValue = scanner.nextLine()
                val intValue = stringValue.toInt()
                if (intValue !in min..max) {
                    throw IllegalArgumentException()
                }
                return intValue
            } catch (e: IllegalArgumentException) {
                printLine(errorMessage)
            }
        }
        throw IllegalArgumentException("Error during reading int value")
    }

    override fun readIntFromRangeWithPrompt(
        min: Int,
        max: Int,
        prompt: String,
        errorMessage: String,
    ): Int {
        printLine(prompt)
        return readIntFromRange(min, max, errorMessage)
    }
}
