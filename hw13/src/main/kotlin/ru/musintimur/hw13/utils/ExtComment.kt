package ru.musintimur.hw13.utils

import ru.musintimur.hw13.dto.CommentDto
import ru.musintimur.hw13.models.Comment

fun Comment.toDto(): CommentDto = CommentDto(id, text, book.id, user?.username.orEmpty())
