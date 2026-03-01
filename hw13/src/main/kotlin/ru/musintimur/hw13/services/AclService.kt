package ru.musintimur.hw13.services

import org.slf4j.LoggerFactory
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.GrantedAuthoritySid
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.MutableAclService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw13.models.Book
import ru.musintimur.hw13.models.Comment
import ru.musintimur.hw13.models.User

@Service
open class AclService(
    private val mutableAclService: MutableAclService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    open fun grantOwnerPermissions(
        domainObject: Any,
        user: User,
    ) {
        try {
            val objectIdentity = ObjectIdentityImpl(domainObject.javaClass, getId(domainObject))
            val mutableAcl = mutableAclService.createAcl(objectIdentity)

            mutableAcl.insertAce(mutableAcl.entries.size, BasePermission.READ, PrincipalSid(user.username), true)
            mutableAcl.insertAce(mutableAcl.entries.size, BasePermission.WRITE, PrincipalSid(user.username), true)
            mutableAcl.insertAce(mutableAcl.entries.size, BasePermission.DELETE, PrincipalSid(user.username), true)

            mutableAclService.updateAcl(mutableAcl)
            logger.debug("ACL permissions granted for user ${user.username} on ${domainObject.javaClass.simpleName}")
        } catch (e: Exception) {
            logger.error("Failed to grant ACL permissions for user ${user.username}", e)
            throw RuntimeException("Failed to initialize ACL permissions", e)
        }
    }

    open fun hasPermission(
        objectId: Long,
        className: String,
        permission: String,
    ): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication?.principal as? User ?: return false

        return try {
            val clazz = Class.forName(className)
            val objectIdentity = ObjectIdentityImpl(clazz, objectId)
            val acl = mutableAclService.readAclById(objectIdentity)

            val permissionInt =
                when (permission.uppercase()) {
                    "READ" -> BasePermission.READ.mask
                    "WRITE" -> BasePermission.WRITE.mask
                    "DELETE" -> BasePermission.DELETE.mask
                    "CREATE" -> BasePermission.CREATE.mask
                    "ADMINISTRATION" -> BasePermission.ADMINISTRATION.mask
                    else -> return false
                }

            val principalSid = PrincipalSid(principal.username)
            val roleSid = GrantedAuthoritySid("ROLE_${principal.role}")

            acl.entries.any { entry ->
                val hasPermission = (entry.permission.mask and permissionInt) == permissionInt
                val hasGranting = entry.isGranting

                val sidMatches =
                    entry.sid.toString() == principalSid.toString() ||
                        entry.sid.toString() == roleSid.toString()

                hasPermission && hasGranting && sidMatches
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun getId(obj: Any): Long =
        when (obj) {
            is Book -> obj.id
            is Comment -> obj.id
            else -> throw IllegalArgumentException("Unknown domain object: ${obj.javaClass}")
        }
}
