
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
class AuthorControllerSecurityTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testAccessToAuthorListWithoutAuthentication() {
        mockMvc
            .perform(get("/authors"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrlPattern("**/login"))
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = ["USER"])
    fun testAccessToAuthorListWithAuthentication() {
        mockMvc
            .perform(get("/authors"))
            .andExpect(status().isOk)
    }
}
