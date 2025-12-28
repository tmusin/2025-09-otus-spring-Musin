package ru.musintimur.hw09.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw09.exceptions.EntityNotFoundException
import ru.musintimur.hw09.models.Book
import ru.musintimur.hw09.repositories.AuthorRepository
import ru.musintimur.hw09.repositories.BookRepository
import ru.musintimur.hw09.repositories.GenreRepository
import java.util.Optional

@Service
open class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val genreRepository: GenreRepository,
) : BookService {
    @Transactional(readOnly = true)
    override fun findById(id: Long): Optional<Book> = bookRepository.findById(id)

    @Transactional(readOnly = true)
    override fun findAll(): List<Book> = bookRepository.findAll()

    @Transactional
    override fun insert(
        title: String,
        authorId: Long,
        genreId: Long,
    ): Book {
        val author =
            authorRepository
                .findById(authorId)
                .orElseThrow { EntityNotFoundException("Author with id $authorId not found") }
        val genre =
            genreRepository
                .findById(genreId)
                .orElseThrow { EntityNotFoundException("Genre with id $genreId not found") }

        val book = Book(title = title, author = author, genre = genre)
        return bookRepository.save(book)
    }

    @Transactional
    override fun update(
        id: Long,
        title: String,
        authorId: Long,
        genreId: Long,
    ): Book {
        val book =
            bookRepository
                .findById(id)
                .orElseThrow { EntityNotFoundException("Book with id $id not found") }

        val author =
            authorRepository
                .findById(authorId)
                .orElseThrow { EntityNotFoundException("Author with id $authorId not found") }
        val genre =
            genreRepository
                .findById(genreId)
                .orElseThrow { EntityNotFoundException("Genre with id $genreId not found") }

        book.title = title
        book.author = author
        book.genre = genre

        return bookRepository.save(book)
    }

    @Transactional
    override fun deleteById(id: Long) {
        bookRepository.deleteById(id)
    }
}
