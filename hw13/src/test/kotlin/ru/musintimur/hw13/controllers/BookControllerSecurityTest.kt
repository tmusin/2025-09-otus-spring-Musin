package ru.musintimur.hw13.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.musintimur.hw13.support.WithRealUser

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookControllerSecurityTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testUnauthenticatedUserCannotAccessBooks() {
        mockMvc
            .perform(get("/books"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/login"))
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun testAuthenticatedUserCanViewBooks() {
        mockMvc
            .perform(get("/books"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun testUserCanAccessBookById() {
        mockMvc
            .perform(get("/books/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun testUnauthenticatedUserCannotAccessCreateBookPage() {
        mockMvc
            .perform(get("/books/new"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/login"))
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun testUserCannotCreateBook() {
        mockMvc
            .perform(
                post("/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"title":"New Book","authorId":1,"genreId":1}"""),
            ).andExpect(status().is5xxServerError)
            .andExpect(content().string("{\"message\":\"Access Denied\",\"status\":500}"))
    }

    @Test
    @WithRealUser(username = "editor1", roles = ["EDITOR"])
    fun testEditorCanCreateBook() {
        mockMvc
            .perform(
                post("/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"title":"Editor Book","authorId":1,"genreId":1}"""),
            ).andExpect(status().isCreated)
    }

    @Test
    @WithRealUser(username = "admin", roles = ["ADMIN"])
    fun testAdminCanCreateBook() {
        mockMvc
            .perform(
                post("/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"title":"Admin Book","authorId":1,"genreId":1}"""),
            ).andExpect(status().isCreated)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun testAdminCanDeleteAnyBook() {
        mockMvc
            .perform(delete("/api/books/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    @WithRealUser(username = "editor1", roles = ["EDITOR"])
    fun testEditorCanDeleteOwnBook() {
        mockMvc
            .perform(delete("/api/books/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    @WithRealUser(username = "editor1", roles = ["EDITOR"])
    fun testEditorCannotDeleteOtherEditorBook() {
        mockMvc
            .perform(delete("/api/books/2"))
            .andExpect(status().is5xxServerError)
            .andExpect(content().string("{\"message\":\"Access Denied\",\"status\":500}"))
    }

    @Test
    @WithRealUser(username = "user1", roles = ["USER"])
    fun testUserCannotDeleteBook() {
        mockMvc
            .perform(delete("/api/books/1"))
            .andExpect(status().is5xxServerError)
            .andExpect(content().string("{\"message\":\"Access Denied\",\"status\":500}"))
    }
}
