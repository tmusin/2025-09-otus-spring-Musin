package ru.musintimur.hw04.commands

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import ru.musintimur.hw04.service.LocalizedIOService
import ru.musintimur.hw04.service.TestRunnerService

@ShellComponent
class TestCommands(
    private val ioService: LocalizedIOService,
    private val testRunnerService: TestRunnerService,
) {
    @ShellMethod(value = "Start test", key = ["start", "run", "test", "go"])
    fun test() {
        ioService.printLineLocalized("TestCommands.test.starting")
        testRunnerService.run()
        ioService.printLineLocalized("TestCommands.test.completed")
    }
}
