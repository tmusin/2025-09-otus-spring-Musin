package ru.musintimur.hw13.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthorRestControllerSecurityTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testUnauthenticatedUserCannotGetAuthors() {
        mockMvc
            .perform(get("/api/authors"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/login"))
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun testAuthenticatedUserCanGetAuthors() {
        mockMvc
            .perform(get("/api/authors"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }
}
