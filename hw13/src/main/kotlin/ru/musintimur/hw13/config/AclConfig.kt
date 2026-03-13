package ru.musintimur.hw13.config

import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl
import org.springframework.security.acls.domain.ConsoleAuditLogger
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy
import org.springframework.security.acls.domain.SpringCacheBasedAclCache
import org.springframework.security.acls.jdbc.BasicLookupStrategy
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.acls.model.AclCache
import org.springframework.security.acls.model.MutableAclService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.sql.DataSource

@Configuration
open class AclConfig {
    @Bean
    open fun cacheManager(): org.springframework.cache.CacheManager = ConcurrentMapCacheManager("aclCache")

    @Bean
    open fun aclAuthorizationStrategy(): AclAuthorizationStrategyImpl = AclAuthorizationStrategyImpl(SimpleGrantedAuthority("ROLE_ADMIN"))

    @Bean
    open fun permissionGrantingStrategy(): DefaultPermissionGrantingStrategy = DefaultPermissionGrantingStrategy(ConsoleAuditLogger())

    @Bean
    open fun aclCache(
        cacheManager: org.springframework.cache.CacheManager,
        aclAuthorizationStrategy: AclAuthorizationStrategyImpl,
        permissionGrantingStrategy: DefaultPermissionGrantingStrategy,
    ): AclCache {
        val cache =
            cacheManager.getCache("aclCache")
                ?: throw IllegalStateException("aclCache not found")
        return SpringCacheBasedAclCache(
            cache,
            permissionGrantingStrategy,
            aclAuthorizationStrategy,
        )
    }

    @Bean
    open fun mutableAclService(
        dataSource: DataSource,
        aclCache: AclCache,
    ): MutableAclService {
        val lookupStrategy =
            BasicLookupStrategy(
                dataSource,
                aclCache,
                AclAuthorizationStrategyImpl(SimpleGrantedAuthority("ROLE_ADMIN")),
                ConsoleAuditLogger(),
            )
        return JdbcMutableAclService(dataSource, lookupStrategy, aclCache)
    }
}
