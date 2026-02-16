package ru.musintimur.hw12.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.Table

@Entity
@Table(name = "comments")
@NamedEntityGraph(
    name = "commnet-book-entity-graph",
    attributeNodes = [
        NamedAttributeNode("book"),
    ],
)
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(name = "text", nullable = false)
    var text: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    var book: Book,
)
