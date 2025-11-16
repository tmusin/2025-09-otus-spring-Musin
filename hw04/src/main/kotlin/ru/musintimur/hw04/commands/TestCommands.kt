package ru.musintimur.hw04.commands

import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellMethodAvailability
import ru.musintimur.hw04.service.LocalizedIOService
import ru.musintimur.hw04.service.StudentService
import ru.musintimur.hw04.service.TestRunnerService

@ShellComponent
class TestCommands(
    private val ioService: LocalizedIOService,
    private val studentService: StudentService,
    private val testRunnerService: TestRunnerService,
) {
    @ShellMethod(value = "Register student", key = ["register", "login"])
    fun register() {
        val student = studentService.determineCurrentStudent()
        ioService.printFormattedLineLocalized(
            "TestCommands.register.success",
            student.firstName,
            student.lastName,
        )
    }

    @ShellMethod(value = "Log Out", key = ["logout"])
    fun logout() {
        studentService.clearCurrentStudent()
        ioService.printLineLocalized("TestCommands.logout.success")
    }

    @ShellMethod(value = "Start test", key = ["start", "run", "test", "go"])
    @ShellMethodAvailability("testAvailability")
    fun test() {
        ioService.printLineLocalized("TestCommands.test.starting")
        testRunnerService.run()
        ioService.printLineLocalized("TestCommands.test.completed")
    }

    fun testAvailability(): Availability =
        if (studentService.isStudentRegistered()) {
            Availability.available()
        } else {
            Availability.unavailable(
                ioService.getMessage("TestCommands.test.not.available"),
            )
        }
}
