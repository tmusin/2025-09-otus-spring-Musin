package ru.musintimur.hw10.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw10.dto.BookCreateDto
import ru.musintimur.hw10.dto.BookDto
import ru.musintimur.hw10.dto.BookUpdateDto
import ru.musintimur.hw10.exceptions.EntityNotFoundException
import ru.musintimur.hw10.models.Book
import ru.musintimur.hw10.repositories.AuthorRepository
import ru.musintimur.hw10.repositories.BookRepository
import ru.musintimur.hw10.repositories.GenreRepository

@Service
open class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val genreRepository: GenreRepository,
) : BookService {
    @Transactional(readOnly = true)
    override fun findById(id: Long): BookDto {
        val book =
            bookRepository
                .findById(id)
                .orElseThrow {
                    EntityNotFoundException("Book with id $id not found")
                }
        return book.toDto()
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<BookDto> = bookRepository.findAll().map { it.toDto() }

    @Transactional
    override fun insert(dto: BookCreateDto): BookDto {
        val author =
            authorRepository
                .findById(dto.authorId ?: 0)
                .orElseThrow { EntityNotFoundException("Author with id ${dto.authorId} not found") }
        val genre =
            genreRepository
                .findById(dto.genreId ?: 0)
                .orElseThrow { EntityNotFoundException("Genre with id ${dto.genreId} not found") }

        val book = Book(title = dto.title, author = author, genre = genre)
        val savedBook = bookRepository.save(book)
        return savedBook.toDto()
    }

    @Transactional
    override fun update(dto: BookUpdateDto): BookDto {
        val book =
            bookRepository
                .findById(dto.id)
                .orElseThrow { EntityNotFoundException("Book with id ${dto.id} not found") }

        val author =
            authorRepository
                .findById(dto.authorId ?: 0)
                .orElseThrow { EntityNotFoundException("Author with id ${dto.authorId} not found") }
        val genre =
            genreRepository
                .findById(dto.genreId ?: 0)
                .orElseThrow { EntityNotFoundException("Genre with id ${dto.genreId} not found") }

        book.title = dto.title
        book.author = author
        book.genre = genre

        val updatedBook = bookRepository.save(book)
        return updatedBook.toDto()
    }

    @Transactional
    override fun deleteById(id: Long) {
        bookRepository.deleteById(id)
    }
}
