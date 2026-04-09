package com.example.library.service

import com.example.library.entity.Author
import com.example.library.entity.Book
import com.example.library.entity.Genre
import com.example.library.entity.Reader
import com.example.library.repository.AuthorRepository
import com.example.library.repository.BookRepository
import com.example.library.repository.GenreRepository
import com.example.library.repository.ReaderRepository
import io.mockk.*
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.util.Optional

class LibraryServiceMockkTest {

    private val authorRepository: AuthorRepository = mockk()
    private val bookRepository: BookRepository = mockk()
    private val genreRepository: GenreRepository = mockk()
    private val readerRepository: ReaderRepository = mockk()
    private lateinit var service: LibraryService

    @BeforeEach
    fun setUp() {
        service = LibraryService(authorRepository, bookRepository, genreRepository, readerRepository)
    }

    @Test
    fun `createAuthor возвращает того же автора что вернул save`() {
        val author = Author(name = "Пушкин")
        val savedAuthor = Author(id = 42L, name = "Пушкин")

        every { authorRepository.save(eq(author)) } returns savedAuthor

        val result = service.createAuthor("Пушкин")

        assertEquals(42L, result.id)
        assertEquals("Пушкин", result.name)
        verify(exactly = 1) { authorRepository.save(eq(author)) }
    }

    @Test
    fun `getAllGenres возвращает список из genreRepository findAll`() {
        val genres = listOf(Genre(id = 1L, name = "Фантастика"), Genre(id = 2L, name = "Детектив"))

        every { genreRepository.findAll() } returns genres

        val result = service.getAllGenres()

        assertEquals(2, result.size)
        assertEquals("Фантастика", result[0].name)
        verify { genreRepository.findAll() }
    }

    @Test
    fun `createBook бросает EntityNotFoundException если автор не найден`() {
        every { authorRepository.findById(eq(99L)) } returns Optional.empty()

        val ex = assertThrows(EntityNotFoundException::class.java) {
            service.createBook("Книга", "isbn-1", 99L, 1L)
        }

        assertEquals("Author not found with id: 99", ex.message)
        verify(exactly = 1) { authorRepository.findById(eq(99L)) }
        verify(exactly = 0) { bookRepository.save(any()) }
    }

    @Test
    fun `createBook передаёт в save книгу с нужным названием и ISBN slot ловит аргумент`() {
        val author = Author(id = 1L, name = "Гоголь")
        val genre = Genre(id = 2L, name = "Классика")
        val bookSlot = slot<Book>()

        every { authorRepository.findById(eq(1L)) } returns Optional.of(author)
        every { genreRepository.findById(eq(2L)) } returns Optional.of(genre)
        every { bookRepository.save(capture(bookSlot)) } answers { bookSlot.captured.copy(id = 100L) }

        service.createBook("Мёртвые души", "isbn-2", 1L, 2L)

        assertEquals("Мёртвые души", bookSlot.captured.title)
        assertEquals("isbn-2", bookSlot.captured.isbn)
        assertEquals(author, bookSlot.captured.author)
        assertEquals(genre, bookSlot.captured.genre)
        verify(exactly = 1) { bookRepository.save(match { it.title == "Мёртвые души" && it.isbn == "isbn-2" }) }
    }

    @Test
    fun `getBooksPage делегирует в bookRepository findAll с постраничностью`() {
        val author = Author(id = 1L, name = "Чехов")
        val genre = Genre(id = 1L, name = "Пьеса")
        val books = listOf(Book(id = 1L, title = "Чайка", isbn = "isbn-3", author = author, genre = genre))
        val pageRequest = PageRequest.of(0, 20, Sort.by("title"))
        val page = PageImpl(books, pageRequest, 1L)

        every { bookRepository.findAll(eq(pageRequest)) } returns page

        val result = service.getBooksPage(page = 0, size = 20)

        assertEquals(1, result.content.size)
        assertEquals("Чайка", result.content[0].title)
        verify(exactly = 1) { bookRepository.findAll(eq(pageRequest)) }
    }
}
