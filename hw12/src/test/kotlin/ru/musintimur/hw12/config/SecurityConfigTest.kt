package ru.musintimur.hw12.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.musintimur.hw12.controllers.AuthorController
import ru.musintimur.hw12.controllers.BookController
import ru.musintimur.hw12.controllers.BookRestController
import ru.musintimur.hw12.controllers.GenreController
import ru.musintimur.hw12.controllers.LoginController
import ru.musintimur.hw12.services.BookService
import ru.musintimur.hw12.services.CustomUserDetailsService

@WebMvcTest(
    controllers = [
        LoginController::class,
        AuthorController::class,
        GenreController::class,
        BookController::class,
        BookRestController::class,
    ],
)
@Import(SecurityConfig::class)
class SecurityConfigTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var bookService: BookService

    @MockitoBean
    private lateinit var customUserDetailsService: CustomUserDetailsService

    @Test
    fun `should redirect to login when accessing protected page without authentication`() {
        mockMvc
            .perform(get("/books"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    fun `should allow access to login page without authentication`() {
        mockMvc
            .perform(get("/login"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["USER"])
    fun `should allow access to protected page with authentication`() {
        mockMvc
            .perform(get("/books"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 401 for REST endpoint without authentication`() {
        mockMvc
            .perform(get("/api/books"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["USER"])
    fun `should allow access to REST endpoint with authentication`() {
        mockMvc
            .perform(get("/api/books"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 401 for REST book details without authentication`() {
        mockMvc
            .perform(get("/api/books/1"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["USER"])
    fun `should allow access to REST book details with authentication`() {
        mockMvc
            .perform(get("/api/books/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should redirect authors to login without authentication`() {
        mockMvc
            .perform(get("/authors"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["USER"])
    fun `should allow access to authors with authentication`() {
        mockMvc
            .perform(get("/authors"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should redirect genres to login without authentication`() {
        mockMvc
            .perform(get("/genres"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["USER"])
    fun `should allow access to genres with authentication`() {
        mockMvc
            .perform(get("/genres"))
            .andExpect(status().isOk)
    }
}
