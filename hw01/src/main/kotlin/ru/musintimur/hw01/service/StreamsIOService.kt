package ru.musintimur.hw01.service

import java.io.PrintStream

class StreamsIOService(private val printStream: PrintStream) : IOService {
    override fun printLine(s: String) {
        printStream.println(s)
    }

    override fun printFormatLine(
        s: String,
        vararg args: Any?,
    ) {
        printStream.printf(s, *args)
    }
}
