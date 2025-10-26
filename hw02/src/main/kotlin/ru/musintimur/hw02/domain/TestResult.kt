package ru.musintimur.hw02.domain

data class TestResult(
    val student: Student,
    val answeredQuestions: MutableList<Question> = mutableListOf(),
) {
    var rightAnswersCount: Int = 0
        private set

    fun applyAnswer(
        question: Question,
        isRightAnswer: Boolean,
    ) {
        answeredQuestions.add(question)
        if (isRightAnswer) {
            rightAnswersCount++
        }
    }
}
