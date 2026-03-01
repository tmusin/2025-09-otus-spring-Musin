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
class CommentRestControllerSecurityTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testUnauthenticatedUserCannotGetComments() {
        mockMvc
            .perform(get("/api/comments?bookId=1"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/login"))
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun testAuthenticatedUserCanGetComments() {
        mockMvc
            .perform(get("/api/comments?bookId=1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun testUnauthenticatedUserCannotCreateComment() {
        mockMvc
            .perform(
                post("/api/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"text":"Test comment","bookId":1}"""),
            ).andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/login"))
    }

    @Test
    @WithRealUser(username = "user1", roles = ["USER"])
    fun testUserCanCreateComment() {
        mockMvc
            .perform(
                post("/api/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"text":"My comment","bookId":3}"""),
            ).andExpect(status().isCreated)
    }

    @Test
    @WithRealUser(username = "editor1", roles = ["EDITOR"])
    fun testEditorCanCreateComment() {
        mockMvc
            .perform(
                post("/api/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"text":"Editor comment","bookId":2}"""),
            ).andExpect(status().isCreated)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun testAdminCanDeleteAnyComment() {
        mockMvc
            .perform(delete("/api/comments/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    @WithRealUser(username = "user1", roles = ["USER"])
    fun testUserCanDeleteOwnComment() {
        mockMvc
            .perform(delete("/api/comments/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    @WithRealUser(username = "user1", roles = ["USER"])
    fun testUserCannotDeleteOtherUserComment() {
        mockMvc
            .perform(delete("/api/comments/2"))
            .andExpect(status().is5xxServerError)
            .andExpect(content().string("{\"message\":\"Access Denied\",\"status\":500}"))
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun testUserCannotDeleteWithoutAuthentication() {
        mockMvc
            .perform(delete("/api/comments/1"))
            .andExpect(status().is5xxServerError)
            .andExpect(content().string("{\"message\":\"Access Denied\",\"status\":500}"))
    }
}
