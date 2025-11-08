package ru.musintimur.hw04

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.type.classreading.MetadataReader
import org.springframework.core.type.classreading.MetadataReaderFactory
import org.springframework.core.type.filter.TypeFilter
import ru.musintimur.hw04.config.AppProperties
import java.lang.reflect.Field
import java.util.Arrays
import java.util.stream.Collectors

@SpringBootTest
class CommonHwTest {
    companion object {
        private const val CONFIGURATION_ANNOTATION_NAME = "org.springframework.context.annotation.Configuration"
    }

    @Test
    fun shouldNotContainConfigurationAnnotationAboveItSelf() {
        Assertions
            .assertThat(AppProperties::class.java.isAnnotationPresent(Configuration::class.java))
            .withFailMessage(
                "Класс свойств не является конфигурацией т.к. " +
                    "конфигурация для создания бинов, а тут просто компонент группирующий свойства приложения",
            ).isFalse()
    }

    @Test
    fun shouldNotContainPropertySourceAnnotationAboveItSelf() {
        Assertions
            .assertThat(AppProperties::class.java.isAnnotationPresent(PropertySource::class.java))
            .withFailMessage(
                "Аннотацию @PropertySource лучше вешать над конфигурацией, " +
                    "а класс свойств ей не является",
            ).isFalse()
    }

    @Test
    fun shouldNotContainFieldInjectedDependenciesOrProperties() {
        val provider = ClassPathScanningCandidateComponentProvider(false)
        provider.addIncludeFilter(
            TypeFilter { mr: MetadataReader, mf: MetadataReaderFactory ->
                val metaData = mr.classMetadata
                val annotationMetaData = mr.annotationMetadata
                val isTest = metaData.className.endsWith("Test")
                val isInterface = metaData.isInterface
                val isConfiguration = annotationMetaData.hasAnnotation(CONFIGURATION_ANNOTATION_NAME)
                val clazz = getBeanClassByName(metaData.className)
                val classContainsFieldInjectedDependenciesOrProperties =
                    Arrays
                        .stream<Field>(clazz.getDeclaredFields())
                        .anyMatch { f: Field -> f.isAnnotationPresent(Autowired::class.java) || f.isAnnotationPresent(Value::class.java) }
                !isTest && !isInterface && !isConfiguration && classContainsFieldInjectedDependenciesOrProperties
            },
        )

        val classesContainsFieldInjectedDependenciesOrProperties =
            provider.findCandidateComponents("ru.musintimur.hw04")

        val classesNames =
            classesContainsFieldInjectedDependenciesOrProperties
                .stream()
                .map<String?> { obj: BeanDefinition -> obj.beanClassName }
                .collect(Collectors.joining("%n"))
        Assertions
            .assertThat<BeanDefinition?>(classesContainsFieldInjectedDependenciesOrProperties)
            .withFailMessage(
                "На курсе все внедрение рекомендовано осуществлять через конструктор (" +
                    "в т.ч. @Value). Следующие классы нарушают это правило: \n$classesNames",
            ).isEmpty()
    }

    private fun getBeanClassByName(beanClassName: String?): Class<*> {
        try {
            return Class.forName(beanClassName)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(e)
        }
    }
}
