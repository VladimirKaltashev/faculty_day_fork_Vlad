package ru.tbank.education.school.lesson5


data class Book(
    val title: String,
    val author: String,
    val year: Int,
    val genre: String
)

data class Person(
    val name: String,
    val age: Int
)

class Library {
    private val books = mutableListOf<Book>()
    private val people = mutableListOf<Person>()
    private val takenBooks = mutableMapOf<Book, Person>() // Кто какую книгу взял

    // Добавляет книгу в библиотеку
    fun addBook(book: Book) {
        books.add(book)
    }

    // Добавляет человека в список посетителей
    fun addPerson(person: Person) {
        people.add(person)
    }

    // Возвращает список всех доступных книг (не взятых)
    fun getAvailableBooks(): List<Book> {
        return books.filter { !takenBooks.containsKey(it) }
    }

    // Возвращает список книг определённого автора
    fun getBooksByAuthor(author: String): List<Book> {
        return books.filter { it.author.equals(author, ignoreCase = true) }
    }

    // Возвращает список книг определённого жанра
    fun getBooksByGenre(genre: String): List<Book> {
        return books.filter { it.genre.equals(genre, ignoreCase = true) }
    }

    // Человек берёт книгу по названию
    fun takeBook(personName: String, bookTitle: String): Boolean {
        // 1. Найти человека по имени
        val person = people.find { it.name.equals(personName, ignoreCase = true) }
        if (person == null) {
            return false // Человек не найден
        }

        // 2. Найти книгу по названию
        val book = books.find {
            it.title.equals(bookTitle, ignoreCase = true) &&
                    !takenBooks.containsKey(it) // Книга должна быть доступна
        }

        // 3. Проверить, что книга существует и доступна
        if (book != null) {
            // 4. Добавить запись в takenBooks
            takenBooks[book] = person
            return true
        }

        // 5. Книга не найдена или уже взята
        return false
    }

    // Возвращает список всех посетителей
    fun getAllPeople(): List<Person> {
        return people.toList() // Возвращаем копию списка
    }

    // Возвращает книгу, которую взял человек (по имени)
    fun getBooksTakenByPerson(personName: String): List<Book> {
        // Найти человека по имени
        val person = people.find { it.name.equals(personName, ignoreCase = true) }

        // Если человек не найден, вернуть пустой список
        if (person == null) {
            return emptyList()
        }

        // Вернуть все книги, которые взял этот человек
        return takenBooks.filter { it.value == person }
            .map { it.key }
    }

    // Возвращает информацию о том, кто взял конкретную книгу
    fun getPersonWhoTookBook(bookTitle: String): Person? {
        // Найти книгу по названию среди взятых книг
        val bookEntry = takenBooks.entries.find {
            it.key.title.equals(bookTitle, ignoreCase = true)
        }

        // Вернуть человека, который взял книгу (или null)
        return bookEntry?.value
    }
}

//Пример использования:
fun main() {
    val library = Library()

    // Добавляем книги
    library.addBook(Book("Война и мир", "Лев Толстой", 1869, "Роман"))
    library.addBook(Book("Преступление и наказание", "Фёдор Достоевский", 1866, "Роман"))
    library.addBook(Book("Мастер и Маргарита", "Михаил Булгаков", 1967, "Фантастика"))

    // Добавляем людей
    library.addPerson(Person("Анна", 25))
    library.addPerson(Person("Иван", 30))

    // Проверяем доступные книги
    println("Доступные книги: ${library.getAvailableBooks().map { it.title }}")

    // Берём книгу
    val success = library.takeBook("Анна", "Мастер и Маргарита")
    println("Книга взята: $success")

    // Проверяем, кто взял книгу
    val person = library.getPersonWhoTookBook("Мастер и Маргарита")
    println("Книгу 'Мастер и Маргарита' взял: ${person?.name}")

    // Проверяем, какие книги взял человек
    println("Анна взяла: ${library.getBooksTakenByPerson("Анна").map { it.title }}")

    // Проверяем доступные книги после взятия
    println("Доступные книги: ${library.getAvailableBooks().map { it.title }}")

    // Книги по жанру
    println("Книги в жанре 'Роман': ${library.getBooksByGenre("Роман").map { it.title }}")
}
