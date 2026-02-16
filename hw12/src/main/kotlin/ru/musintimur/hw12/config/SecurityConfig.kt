package ru.musintimur.hw12.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher
import ru.musintimur.hw12.services.CustomUserDetailsService

@Configuration
@EnableWebSecurity
open class SecurityConfig(
    private val userDetailsService: CustomUserDetailsService,
) {
    @Bean
    open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    open fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/login", "/css/**", "/js/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.exceptionHandling { exceptions ->
                exceptions
                    .defaultAuthenticationEntryPointFor(
                        HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        PathPatternRequestMatcher.withDefaults().matcher("/api/**"),
                    ).defaultAuthenticationEntryPointFor(
                        LoginUrlAuthenticationEntryPoint("/login"),
                        PathPatternRequestMatcher.withDefaults().matcher("/**"),
                    )
            }.formLogin { login ->
                login
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
            }.logout { logout ->
                logout
                    .logoutSuccessUrl("/login")
                    .permitAll()
            }.csrf { csrf -> csrf.disable() }

        return http.build()
    }
}
