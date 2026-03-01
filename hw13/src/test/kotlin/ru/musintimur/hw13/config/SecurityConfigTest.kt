package ru.musintimur.hw13.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SecurityConfigTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testPublicPagesAccessible() {
        mockMvc
            .perform(get("/"))
            .andExpect(status().isOk)

        mockMvc
            .perform(get("/login"))
            .andExpect(status().isOk)
    }

    @Test
    @WithAnonymousUser
    fun testAnonymousUserRedirectedToLogin() {
        mockMvc
            .perform(get("/books"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/login"))
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun testUserCanAccessUserEndpoints() {
        mockMvc
            .perform(get("/books"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun testAdminCanAccessAllEndpoints() {
        mockMvc
            .perform(get("/books"))
            .andExpect(status().isOk)

        mockMvc
            .perform(get("/authors"))
            .andExpect(status().isOk)

        mockMvc
            .perform(get("/genres"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(roles = ["EDITOR"])
    fun testEditorCanAccessEditEndpoints() {
        mockMvc
            .perform(get("/books"))
            .andExpect(status().isOk)
    }
}
