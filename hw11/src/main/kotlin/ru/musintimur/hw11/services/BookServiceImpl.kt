package ru.musintimur.hw11.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.musintimur.hw11.dto.BookCreateDto
import ru.musintimur.hw11.dto.BookDto
import ru.musintimur.hw11.dto.BookUpdateDto
import ru.musintimur.hw11.exceptions.EntityNotFoundException
import ru.musintimur.hw11.models.Book
import ru.musintimur.hw11.repositories.AuthorRepository
import ru.musintimur.hw11.repositories.BookRepository
import ru.musintimur.hw11.repositories.GenreRepository

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
            .flatMap { book ->
                Mono
                    .zip(
                        authorRepository
                            .findById(book.authorId)
                            .map { it.fullName }
                            .switchIfEmpty(Mono.just("")),
                        genreRepository
                            .findById(book.genreId)
                            .map { it.name }
                            .switchIfEmpty(Mono.just("")),
                    ).map { tuple ->
                        book.toDto(tuple.t1, tuple.t2)
                    }
            }

    override fun findAll(): Flux<BookDto> =
        bookRepository
            .findAll()
            .flatMap { book ->
                Mono
                    .zip(
                        authorRepository
                            .findById(book.authorId)
                            .map { it.fullName }
                            .switchIfEmpty(Mono.just("")),
                        genreRepository
                            .findById(book.genreId)
                            .map { it.name }
                            .switchIfEmpty(Mono.just("")),
                    ).map { tuple ->
                        book.toDto(tuple.t1, tuple.t2)
                    }
            }

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
                val book =
                    Book(
                        title = dto.title,
                        authorId = dto.authorId.orEmpty(),
                        genreId = dto.genreId.orEmpty(),
                    )
                bookRepository
                    .save(book)
                    .map { savedBook ->
                        savedBook.toDto(tuple.t1.fullName, tuple.t2.name)
                    }
            }

    override fun update(dto: BookUpdateDto): Mono<BookDto> =
        bookRepository
            .findById(dto.id)
            .switchIfEmpty(Mono.error(EntityNotFoundException("Book with id ${dto.id} not found")))
            .flatMap { book ->
                Mono
                    .zip(
                        authorRepository
                            .findById(dto.authorId.orEmpty())
                            .switchIfEmpty(Mono.error(EntityNotFoundException("Author with id ${dto.authorId} not found"))),
                        genreRepository
                            .findById(dto.genreId.orEmpty())
                            .switchIfEmpty(Mono.error(EntityNotFoundException("Genre with id ${dto.genreId} not found"))),
                    ).flatMap { tuple ->
                        val updatedBook =
                            book.apply {
                                title = dto.title
                                authorId = dto.authorId.orEmpty()
                                genreId = dto.genreId.orEmpty()
                            }
                        bookRepository
                            .save(updatedBook)
                            .map { savedBook ->
                                savedBook.toDto(tuple.t1.fullName, tuple.t2.name)
                            }
                    }
            }

    override fun deleteById(id: String): Mono<Void> = bookRepository.deleteById(id)
}
