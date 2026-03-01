package ru.musintimur.hw13.services

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import ru.musintimur.hw13.support.WithRealUser

@SpringBootTest
class AclServiceSecurityTest {
    @Autowired
    private lateinit var aclService: AclService

    @Test
    @WithRealUser(username = "admin")
    fun testAdminHasAllPermissionsOnExistingBook() {
        val hasReadPermission = aclService.hasPermission(1, "ru.musintimur.hw13.models.Book", "READ")
        val hasWritePermission = aclService.hasPermission(1, "ru.musintimur.hw13.models.Book", "WRITE")
        val hasDeletePermission = aclService.hasPermission(1, "ru.musintimur.hw13.models.Book", "DELETE")

        assertTrue(hasReadPermission, "Admin должен иметь READ на любую книгу")
        assertTrue(hasWritePermission, "Admin должен иметь WRITE на любую книгу")
        assertTrue(hasDeletePermission, "Admin должен иметь DELETE на любую книгу")
    }

    @Test
    @WithRealUser(username = "editor1")
    fun testEditorCanAccessOwnBook() {
        val hasReadPermission = aclService.hasPermission(1, "ru.musintimur.hw13.models.Book", "READ")
        val hasWritePermission = aclService.hasPermission(1, "ru.musintimur.hw13.models.Book", "WRITE")
        val hasDeletePermission = aclService.hasPermission(1, "ru.musintimur.hw13.models.Book", "DELETE")

        assertTrue(hasReadPermission, "Owner может читать свою книгу")
        assertTrue(hasWritePermission, "Owner может редактировать свою книгу")
        assertTrue(hasDeletePermission, "Owner может удалять свою книгу")
    }

    @Test
    @WithRealUser(username = "user1")
    fun testUserCanAccessOwnComment() {
        val hasReadPermission = aclService.hasPermission(1, "ru.musintimur.hw13.models.Comment", "READ")
        val hasDeletePermission = aclService.hasPermission(1, "ru.musintimur.hw13.models.Comment", "DELETE")

        assertTrue(hasReadPermission, "Owner может читать свой комментарий")
        assertTrue(hasDeletePermission, "Owner может удалять свой комментарий")
    }

    @Test
    @WithRealUser(username = "user1")
    fun testUserCannotAccessOtherUserComment() {
        val hasDeletePermission = aclService.hasPermission(2, "ru.musintimur.hw13.models.Comment", "DELETE")

        assertTrue(!hasDeletePermission, "User не должен иметь DELETE на чужой комментарий")
    }
}
