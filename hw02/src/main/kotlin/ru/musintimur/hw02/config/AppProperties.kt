package ru.musintimur.hw02.config

data class AppProperties(
    override val rightAnswersCountToPass: Int,
    override val testFileName: String,
) : TestConfig, TestFileNameProvider
