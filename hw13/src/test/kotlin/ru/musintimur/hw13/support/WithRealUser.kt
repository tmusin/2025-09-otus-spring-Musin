
package ru.musintimur.hw13.support

import org.springframework.security.test.context.support.WithSecurityContext

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithRealUserSecurityContextFactory::class)
annotation class WithRealUser(
    val username: String = "user1",
    val roles: Array<String> = ["USER"],
)
