package ru.musintimur.hw05.services

import org.springframework.stereotype.Service
import ru.musintimur.hw05.exceptions.EntityNotFoundException
import ru.musintimur.hw05.models.Book
import ru.musintimur.hw05.repositories.AuthorRepository
import ru.musintimur.hw05.repositories.BookRepository
import ru.musintimur.hw05.repositories.GenreRepository
import java.util.Optional

@Service
class BookServiceImpl(
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository,
    private val genreRepository: GenreRepository,
) : BookService {
    override fun findById(id: Long): Optional<Book> = bookRepository.findById(id)

    override fun findAll(): List<Book> = bookRepository.findAll()

    override fun insert(
        title: String,
        authorId: Long,
        genreId: Long,
    ): Book = save(0, title, authorId, genreId)

    override fun update(
        id: Long,
        title: String,
        authorId: Long,
        genreId: Long,
    ): Book = save(id, title, authorId, genreId)

    override fun deleteById(id: Long) {
        bookRepository.deleteById(id)
    }

    private fun save(
        id: Long,
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
        val book = Book(id, title, author, genre)
        return bookRepository.save(book)
    }
}
