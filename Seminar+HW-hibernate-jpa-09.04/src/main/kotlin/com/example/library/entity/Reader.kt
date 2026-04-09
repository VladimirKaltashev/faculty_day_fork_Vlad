package com.example.library.entity

import jakarta.persistence.*

@Entity
@Table(name = "readers")
data class Reader(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String = "",
    val email: String = "",

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "reader_books",
        joinColumns = [JoinColumn(name = "reader_id")],
        inverseJoinColumns = [JoinColumn(name = "book_id")]
    )
    val books: MutableSet<Book> = mutableSetOf()
)
