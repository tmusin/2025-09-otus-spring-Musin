package ru.musintimur.hw04.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.Locale

@ConfigurationProperties(prefix = "test")
class AppProperties :
    TestConfig,
    TestFileNameProvider,
    LocaleConfig {
    override var rightAnswersCountToPass: Int = 0

    override var locale: Locale = Locale.getDefault()
        private set

    private var fileNameByLocaleTag: Map<String, String> = emptyMap()

    fun setLocale(locale: String) {
        this.locale = Locale.forLanguageTag(locale)
    }

    fun setFileNameByLocaleTag(fileNameByLocaleTag: Map<String, String>) {
        this.fileNameByLocaleTag = fileNameByLocaleTag
    }

    override val testFileName: String
        get() =
            fileNameByLocaleTag[locale.toLanguageTag()]
                ?: throw IllegalStateException("No file name found for locale: ${locale.toLanguageTag()}")
}
