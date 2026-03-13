package ru.musintimur.hw13.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.acls.model.AclService
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
open class SecurityConfig {
    @Bean
    open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/login", "/", "/css/**", "/js/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/books", "/books/*")
                    .authenticated()
                    .requestMatchers("/books/create", "/books/*/edit", "/books/*/delete")
                    .hasAnyRole("ADMIN", "EDITOR")
                    .requestMatchers(HttpMethod.GET, "/api/books", "/api/books/*")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/books")
                    .hasAnyRole("ADMIN", "EDITOR")
                    .requestMatchers(HttpMethod.PUT, "/api/books/*")
                    .hasAnyRole("ADMIN", "EDITOR")
                    .requestMatchers(HttpMethod.DELETE, "/api/books/*")
                    .hasAnyRole("ADMIN", "EDITOR")
                    .requestMatchers(HttpMethod.GET, "/api/comments/**")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/comments")
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/comments/*")
                    .authenticated()
                    .requestMatchers("/authors/**", "/genres/**", "/api/authors/**", "/api/genres/**")
                    .authenticated()
                    .anyRequest()
                    .authenticated()
            }.formLogin {
                it
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/books")
            }.exceptionHandling { handling ->
                handling.authenticationEntryPoint { request, response, authException ->
                    response.sendRedirect("/login")
                }
            }.logout { it.permitAll() }
        return http.build()
    }

    @Bean
    open fun methodSecurityExpressionHandler(aclService: AclService): MethodSecurityExpressionHandler {
        val handler = DefaultMethodSecurityExpressionHandler()
        handler.setPermissionEvaluator(AclPermissionEvaluator(aclService))
        return handler
    }
}
