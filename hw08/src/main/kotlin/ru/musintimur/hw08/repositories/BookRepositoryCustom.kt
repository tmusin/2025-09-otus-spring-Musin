package ru.musintimur.hw08.repositories

interface BookRepositoryCustom {
    fun updateAuthorInBooks(
        authorId: String,
        newAuthorName: String,
    )

    fun updateGenreInBooks(
        genreId: String,
        newGenreName: String,
    )
}
