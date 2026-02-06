package ru.musintimur.hw11.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.dto.BookCreateDto
import ru.musintimur.hw11.dto.BookDto
import ru.musintimur.hw11.dto.BookUpdateDto
import ru.musintimur.hw11.exceptions.EntityNotFoundException
import ru.musintimur.hw11.models.Author
import ru.musintimur.hw11.models.Book
import ru.musintimur.hw11.models.Genre
import ru.musintimur.hw11.repositories.AuthorRepository
import ru.musintimur.hw11.repositories.BookRepository
import ru.musintimur.hw11.repositories.GenreRepository
import ru.musintimur.hw11.utils.toDto

@Service
class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val genreRepository: GenreRepository,
) : BookService {
    override fun findById(id: String): Mono<BookDto> =
        bookRepository
            .findById(id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Book with id $id not found")))
            .map { it.toDto() }

    override fun findAll(): Flux<BookDto> = bookRepository.findAll().map { it.toDto() }

    override fun insert(dto: BookCreateDto): Mono<BookDto> =
        Mono
            .zip(
                authorRepository
                    .findById(dto.authorId.orEmpty())
                    .switchIfEmpty(Mono.error(EntityNotFoundException("Author with id ${dto.authorId} not found"))),
                genreRepository
                    .findById(dto.genreId.orEmpty())
                    .switchIfEmpty(Mono.error(EntityNotFoundException("Genre with id ${dto.genreId} not found"))),
            ).flatMap { tuple ->
                val author = Author(tuple.t1.id.orEmpty(), tuple.t1.fullName)
                val genre = Genre(tuple.t2.id.orEmpty(), tuple.t2.name)

                val book =
                    Book(
                        title = dto.title,
                        author = author,
                        genre = genre,
                    )
                bookRepository
                    .save(book)
                    .map { it.toDto() }
            }

    override fun update(dto: BookUpdateDto): Mono<BookDto> =
        Mono
            .zip(
                authorRepository
                    .findById(dto.authorId!!)
                    .switchIfEmpty(Mono.error(EntityNotFoundException("Author with id ${dto.authorId} not found"))),
                genreRepository
                    .findById(dto.genreId!!)
                    .switchIfEmpty(Mono.error(EntityNotFoundException("Genre with id ${dto.genreId} not found"))),
            ).flatMap { tuple ->
                bookRepository
                    .findById(dto.id)
                    .switchIfEmpty(Mono.error(EntityNotFoundException("Book with id ${dto.id} not found")))
                    .flatMap { book ->
                        val author = Author(tuple.t1.id.orEmpty(), tuple.t1.fullName)
                        val genre = Genre(tuple.t2.id.orEmpty(), tuple.t2.name)

                        val updatedBook =
                            book.apply {
                                this.title = dto.title
                                this.author = author
                                this.genre = genre
                            }
                        bookRepository
                            .save(updatedBook)
                            .map { it.toDto() }
                    }
            }

    override fun deleteById(id: String): Mono<Void> = bookRepository.deleteById(id)
}
