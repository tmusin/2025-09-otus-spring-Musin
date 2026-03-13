package ru.musintimur.hw13.services

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw13.dto.CommentCreateDto
import ru.musintimur.hw13.dto.CommentDto
import ru.musintimur.hw13.models.Comment
import ru.musintimur.hw13.models.User
import ru.musintimur.hw13.repositories.BookRepository
import ru.musintimur.hw13.repositories.CommentRepository
import ru.musintimur.hw13.utils.toDto

@Service
open class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val bookRepository: BookRepository,
    private val aclService: AclService,
) : CommentService {
    @Transactional(readOnly = true)
    override fun findAllByBookId(bookId: Long): List<CommentDto> =
        commentRepository
            .findAllByBookId(bookId)
            .map { CommentDto(it.id, it.text, bookId, it.user?.username.orEmpty()) }

    @Transactional
    override fun save(dto: CommentCreateDto): CommentDto {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication?.principal as? User ?: throw IllegalArgumentException("User not found")
        val book = bookRepository.findById(dto.bookId).orElseThrow()
        val comment = Comment(dto.id, dto.text, book, user)
        val savedComment = commentRepository.save(comment)
        aclService.grantOwnerPermissions(savedComment, user)

        return savedComment.toDto()
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @aclService.hasPermission(#id, 'ru.musintimur.hw13.models.Comment', 'DELETE')")
    override fun deleteById(id: Long) {
        commentRepository.deleteById(id)
    }
}
