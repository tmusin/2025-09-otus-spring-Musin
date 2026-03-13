package ru.musintimur.hw13.services

import ru.musintimur.hw13.models.User

interface AclService {
    fun grantOwnerPermissions(
        domainObject: Any,
        user: User,
    )

    fun hasPermission(
        objectId: Long,
        className: String,
        permission: String,
    ): Boolean
}
