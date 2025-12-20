package ru.musintimur.hw08.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.musintimur.hw08.exceptions.EntityNotFoundException
import ru.musintimur.hw08.models.Book
import ru.musintimur.hw08.repositories.AuthorRepository
import ru.musintimur.hw08.repositories.BookRepository
import ru.musintimur.hw08.repositories.CommentRepository
import ru.musintimur.hw08.repositories.GenreRepository
import java.util.Optional

@Service
open class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val genreRepository: GenreRepository,
    private val commentRepository: CommentRepository,
) : BookService {
    override fun findById(id: String): Optional<Book> = bookRepository.findById(id)

    override fun findAll(): List<Book> = bookRepository.findAll()

    override fun insert(
        title: String,
        authorId: String,
        genreId: String,
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

    override fun update(
        id: String,
        title: String,
        authorId: String,
        genreId: String,
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

        book.apply {
            this.title = title
            this.author = author
            this.genre = genre
        }
        return bookRepository.save(book)
    }

    @Transactional
    override fun deleteById(id: String) {
        bookRepository.deleteById(id)
    }
}
