package ru.musintimur.hw06.models

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
import jakarta.persistence.NamedSubgraph
import jakarta.persistence.Table

@Entity
@Table(name = "comments")
@NamedEntityGraph(
    name = "comment-with-book-details",
    attributeNodes = [
        NamedAttributeNode(
            value = "book",
            subgraph = "book-subgraph",
        ),
    ],
    subgraphs = [
        NamedSubgraph(
            name = "book-subgraph",
            attributeNodes = [
                NamedAttributeNode("author"),
                NamedAttributeNode("genre"),
            ],
        ),
    ],
)
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "text", nullable = false)
    val text: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    val book: Book,
)
