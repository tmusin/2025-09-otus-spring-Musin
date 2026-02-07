package ru.musintimur.hw12.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class BookRestControllerSecurityTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testAccessToRestBooksWithoutAuthentication() {
        mockMvc
            .perform(get("/api/books"))
            .andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["USER"])
    fun testAccessToRestBooksWithAuthentication() {
        mockMvc
            .perform(get("/api/books"))
            .andExpect(status().isOk)
    }

    @Test
    fun testAccessToRestBookDetailsWithoutAuthentication() {
        mockMvc
            .perform(get("/api/books/1"))
            .andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["USER"])
    fun testAccessToRestBookDetailsWithAuthentication() {
        mockMvc
            .perform(get("/api/books/1"))
            .andExpect(status().isOk)
    }
}
