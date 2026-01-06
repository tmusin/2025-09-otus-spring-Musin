package ru.musintimur.hw09.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw09.dto.BookCreateDto
import ru.musintimur.hw09.dto.BookDto
import ru.musintimur.hw09.dto.BookIdDto
import ru.musintimur.hw09.dto.BookListItemDto
import ru.musintimur.hw09.dto.BookUpdateDto
import ru.musintimur.hw09.exceptions.EntityNotFoundException
import ru.musintimur.hw09.models.Book
import ru.musintimur.hw09.repositories.AuthorRepository
import ru.musintimur.hw09.repositories.BookRepository
import ru.musintimur.hw09.repositories.GenreRepository

@Service
open class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val genreRepository: GenreRepository,
) : BookService {
    @Transactional(readOnly = true)
    override fun findById(dto: BookIdDto): BookDto? {
        val book = bookRepository.findById(dto.id).orElse(null) ?: return null
        return book.toDto()
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<BookListItemDto> = bookRepository.findAll().map { it.toListItemDto() }

    @Transactional
    override fun insert(dto: BookCreateDto): BookDto {
        val author =
            authorRepository
                .findById(dto.authorId)
                .orElseThrow { EntityNotFoundException("Author with id ${dto.authorId} not found") }
        val genre =
            genreRepository
                .findById(dto.genreId)
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
                .findById(dto.authorId)
                .orElseThrow { EntityNotFoundException("Author with id ${dto.authorId} not found") }
        val genre =
            genreRepository
                .findById(dto.genreId)
                .orElseThrow { EntityNotFoundException("Genre with id ${dto.genreId} not found") }

        book.title = dto.title
        book.author = author
        book.genre = genre

        val updatedBook = bookRepository.save(book)
        return updatedBook.toDto()
    }

    @Transactional
    override fun deleteById(dto: BookIdDto) {
        bookRepository.deleteById(dto.id)
    }
}
