package ru.musintimur.hw07.models

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.reflections.ReflectionUtils
import org.reflections.Reflections
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.Map
import java.util.Objects
import java.util.stream.Stream

internal class ModelsCommonTest {
    @ParameterizedTest
    @MethodSource("getEntities")
    fun shouldBeNoOneToOneRelationshipsInModelClasses(entityClass: Class<*>) {
        val oneToOneRelationshipExists =
            entityClass
                .getDeclaredFields()
                .any { field -> field.isAnnotationPresent(OneToOne::class.java) }
        Assertions
            .assertThat(oneToOneRelationshipExists)
            .withFailMessage("В доменной модели ДЗ не предусмотрены связи OneToOne")
            .isFalse()
    }

    @ParameterizedTest
    @MethodSource("getEntities")
    fun shouldBeNoEagerRelationshipsInModelClasses(entityClass: Class<*>) {
        val eagerFetchExists =
            entityClass
                .getDeclaredFields()
                .mapNotNull {
                    getRelationAnnotationArgumentValue(
                        it,
                        "fetch",
                        FetchType::class.java,
                    )
                }.any { it == FetchType.EAGER }
        Assertions
            .assertThat(eagerFetchExists)
            .withFailMessage("Лучше все связи сделать LAZY")
            .isFalse()
    }

    @ParameterizedTest
    @MethodSource("getEntities")
    fun shouldMappedForBidirectionalRelationshipsInModelClasses(entityClass: Class<*>) {
        val relationsEntries = findAllRelationsEntry(entityClass)
        val hasBidirectionalRelationshipsWithoutMappedBy =
            relationsEntries.entries
                .any { relationEntry ->
                    val reverseRelations = findAllRelationsEntry(relationEntry.key!!)
                    val reverseRelationField = reverseRelations.get(entityClass)
                    if (Objects.isNull(reverseRelationField)) {
                        return@any false
                    }
                    val relationFieldName = relationEntry.value?.name
                    val reverseRelationFieldName = reverseRelationField?.name
                    val mappedByValue =
                        getRelationAnnotationArgumentValue(
                            relationEntry.value,
                            "mappedBy",
                            String::class.java,
                        )
                    val reverseMappedByValue =
                        getRelationAnnotationArgumentValue(
                            reverseRelationField,
                            "mappedBy",
                            String::class.java,
                        )
                    reverseRelationFieldName != mappedByValue && relationFieldName != reverseMappedByValue
                }
        Assertions
            .assertThat(hasBidirectionalRelationshipsWithoutMappedBy)
            .withFailMessage("Двунаправленные связи должны быть настроены с помощью mappedBy")
            .isFalse()
    }

    private fun <T> getRelationAnnotationArgumentValue(
        field: Field?,
        argumentName: String?,
        returnType: Class<T>,
    ): T? =
        field
            ?.annotations
            ?.filterNotNull()
            ?.flatMap { a: Annotation ->
                a.javaClass
                    .getDeclaredMethods()
                    .map { m: Method? ->
                        Map.entry<Method, Annotation>(
                            m,
                            a,
                        )
                    }
            }?.filter { e -> e?.key?.name == argumentName }
            ?.map { e ->
                ReflectionUtils.invoke(
                    e.key,
                    e.value,
                )
            }?.map { obj: Any? -> returnType.cast(obj) }
            ?.firstOrNull()

    private fun findAllRelationsEntry(entityClass: Class<*>): MutableMap<out Class<*>?, Field?> =
        entityClass
            .getDeclaredFields()
            .filterNot { it.type.isPrimitive }
            .map { f -> Map.entry(f, fieldToClass(f)) }
            .filter { e -> entitiesClasses?.contains(e.value) == true }
            .associate { it.value to it.key }
            .toMutableMap()

    private fun fieldToClass(field: Field): Class<*> {
        var className = field.type.name
        if (MutableCollection::class.java.isAssignableFrom(field.type)) {
            className =
                field.genericType.typeName
                    .split("<".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
                    .split(">".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0]
        }
        try {
            return Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private var entitiesClasses: MutableSet<Class<*>?>? = null

        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            val reflections: Reflections = Reflections("ru.musintimur.hw07.models")
            entitiesClasses = reflections.getTypesAnnotatedWith(Entity::class.java)
        }

        @JvmStatic
        private fun getEntities(): Stream<Arguments> = entitiesClasses!!.stream().map { arguments: Class<*>? -> Arguments.of(arguments) }
    }
}
