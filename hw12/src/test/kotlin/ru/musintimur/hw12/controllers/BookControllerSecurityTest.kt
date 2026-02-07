package ru.musintimur.hw12.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerSecurityTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testAccessToBookListWithoutAuthentication() {
        mockMvc
            .perform(get("/books"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["USER"])
    fun testAccessToBookListWithAuthentication() {
        mockMvc
            .perform(get("/books"))
            .andExpect(status().isOk)
    }

    @Test
    fun testLoginPageAccessible() {
        mockMvc
            .perform(get("/login"))
            .andExpect(status().isOk)
    }

    @Test
    fun testAccessToBookDetailsWithoutAuthentication() {
        mockMvc
            .perform(get("/books/1"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["USER"])
    fun testAccessToBookDetailsWithAuthentication() {
        mockMvc
            .perform(get("/books/1"))
            .andExpect(status().isOk)
    }
}
