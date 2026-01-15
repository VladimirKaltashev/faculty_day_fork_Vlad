package ru.tbank.education.school.lesson7

data class User(
    val name: String,
    val orders: List<Order>
)

data class Order(
    val id: Long,
    val product: String,
    val amount: Long,
    var isPaid: Boolean = false,
    var isDelivered: Boolean = false
)

// Задание 1 - выведите все заказы
fun task1() {
    val users = listOf(
        User(
            "Анна", listOf(
                Order(1, "Телефон", 10000),
                Order(2, "Чехол", 100)
            )
        ),
        User(
            "Борис", listOf(
                Order(3, "Книга", 50),
                Order(4, "Рюкзак", 200)
            )
        )
    )

    val allOrders = users.flatMap { it.orders }
    allOrders.forEach { order ->
        println("Заказ ${order.id}: ${order.product} - ${order.amount} руб.")
    }
}

// Задание 2 - выведите отчет в формате месяц-прибыль
fun task2() {
    val months = listOf("Янв", "Фев", "Мар", "Апр", "Май")
    val revenue = listOf(1000, 1200, 800, 1400, 1300)

    val report = months.zip(revenue) { month, rev ->
        "$month - $rev"
    }
    report.forEach { println(it) }
}

// Задание 3 - выведите id всех заказов, которые были доставлены и оплачены на сумму > 1000
fun task3() {
    val orders = listOf(
        Order(id = 1, product = "Чехол", amount = 1200, isPaid = true, isDelivered = false),
        Order(id = 2, product = "Телефон", amount = 20000, isPaid = true, isDelivered = false),
        Order(id = 3, product = "Рюкзак", amount = 1000, isPaid = true, isDelivered = true),
        Order(id = 4, product = "Кружка", amount = 500, isPaid = false, isDelivered = false)
    )

    val filteredOrderIds = orders
        .filter { it.isPaid && it.isDelivered && it.amount > 1000 }
        .map { it.id }

    println("ID заказов (оплачены, доставлены, сумма > 1000): $filteredOrderIds")
}

data class Student(
    val name: String,
    val group: String
)

// Задание 4 - выведите кажду группу и студентов в ней
fun task4() {
    val students = listOf(
        Student(name = "Анна", group = "A-01"),
        Student(name = "Борис", group = "A-02"),
        Student(name = "Виктор", group = "A-01"),
        Student(name = "Галина", group = "A-03"),
        Student(name = "Денис", group = "A-02")
    )

    val grouped = students.groupBy { it.group }
    grouped.forEach { (group, groupStudents) ->
        println("Группа $group:")
        groupStudents.forEach { student ->
            println("  - ${student.name}")
        }
    }
}

data class ApiResponse(val code: Int, val message: String)

// Задание 5 - получить первый успешный ответ и последний ответ с ошибкой на стороне сервера
fun task5() {
    val responses = listOf(
        ApiResponse(code = 500, message = "Internal Error"),
        ApiResponse(code = 404, message = "Not Found"),
        ApiResponse(code = 200, message = "OK"),
        ApiResponse(code = 200, message = "Cached OK")
    )

    val firstSuccess = responses.firstOrNull { it.code == 200 }
    val lastServerError = responses.lastOrNull { it.code in 500..599 }

    println("Первый успешный ответ: $firstSuccess")
    println("Последняя серверная ошибка: $lastServerError")
}

data class Movie(val title: String, val rating: Double)

// Задание 6 - получить топ 3 фильма по рейтингу
fun task6() {
    val movies = listOf(
        Movie(title = "Интерстеллар", rating = 9.0),
        Movie(title = "Начало", rating = 8.8),
        Movie(title = "Дюна", rating = 8.2),
        Movie(title = "Тёмный рыцарь", rating = 9.1),
        Movie(title = "Мементо", rating = 8.5)
    )

    val top3 = movies.sortedByDescending { it.rating }.take(3)
    println("Топ 3 фильма:")
    top3.forEachIndexed { index, movie ->
        println("${index + 1}. ${movie.title} - ${movie.rating}")
    }
}

// Задание 7 - добавить логирование операций
fun task7() {
    val add: (a: Int, b: Int) -> Int = { a, b -> a + b }
    val subtract: (a: Int, b: Int) -> Int = { a, b -> a - b }
    val multiply: (a: Int, b: Int) -> Int = { a, b -> a * b }

    val addWithLog = { a: Int, b: Int ->
        println("Выполняем сложение: $a + $b")
        val result = add(a, b)
        println("Результат: $result")
        result
    }

    val subtractWithLog = { a: Int, b: Int ->
        println("Выполняем вычитание: $a - $b")
        val result = subtract(a, b)
        println("Результат: $result")
        result
    }

    val multiplyWithLog = { a: Int, b: Int ->
        println("Выполняем умножение: $a * $b")
        val result = multiply(a, b)
        println("Результат: $result")
        result
    }

    println("Тестирование функций с логированием:")
    addWithLog(5, 3)
    subtractWithLog(10, 4)
    multiplyWithLog(6, 7)
}

data class Client(
    val name: String,
    val email: String,
    val phone: String,
)

// Задание 8 - функции для валидации полей
fun task8() {
    val rawClients = listOf(
        Client(name = "  Иван  ", email = "  IVAN@MAIL.RU  ", phone = " +7 (999) 123-45-67 "),
        Client(name = "  Мария  ", email = "maria@mail.ru", phone = "8-800-555-35-35"),
        Client(name = " ", email = "test@", phone = "000"),
    )

    val trimName: (Client) -> Client = { client ->
        client.copy(name = client.name.trim())
    }

    val normalizeEmail: (Client) -> Client = { client ->
        client.copy(email = client.email.trim().lowercase())
    }

    val normalizePhone: (Client) -> Client = { client ->
        client.copy(phone = client.phone.trim().replace(Regex("[^\\d+]"), ""))
    }

    val validateName: (Client) -> Boolean = { client ->
        client.name.isNotBlank() && client.name.length >= 2
    }

    val validateEmail: (Client) -> Boolean = { client ->
        client.email.contains('@') && client.email.contains('.')
    }

    val validatePhone: (Client) -> Boolean = { client ->
        client.phone.length >= 10
    }

    val processClient = trimName
        .andThen(normalizeEmail)
        .andThen(normalizePhone)

    println("Обработанные клиенты:")
    rawClients.map { processClient(it) }.forEachIndexed { index, client ->
        println("Клиент ${index + 1}:")
        println("  Имя: '${client.name}' - ${if (validateName(client)) "✓" else "✗"}")
        println("  Email: '${client.email}' - ${if (validateEmail(client)) "✓" else "✗"}")
        println("  Телефон: '${client.phone}' - ${if (validatePhone(client)) "✓" else "✗"}")
    }
}

// Задание 9 - просто смотрим на примеры
fun task9() {
    val student = "Коля" to 14  // Создаем пару (Имя, Возраст)
    val subject = "Математика" to 5  // Создаем пару (Предмет, Оценка)

    println("Ученик: ${student.first}, возраст: ${student.second}")
    println("Предмет: ${subject.first}, оценка: ${subject.second}")

    print("Обратный отсчёт: ")
    for (i in 5 downTo 1) {
        print("$i..")
    }
    println()

    print("Чётные числа от 2 до 10: ")
    for (num in 2..10 step 2) {
        print("$num ")
    }
    println()

    val fruits = listOf("яблоко", "банан", "апельсин", "груша")
    print("Первые 3 фрукта: ")
    for (i in 0 until 3) {  // Только 0, 1, 2 (без последнего)
        print("${fruits[i]} ")
    }
    println()
}

// Задание 10 - функция деления с использованием runCatching и Result<T>
fun task10() {
    fun safeDivide(a: Int, b: Int): Result<Int> = runCatching {
        if (b == 0) throw ArithmeticException("Деление на ноль!")
        a / b
    }

    val testCases = listOf(
        10 to 2,
        5 to 0,
        0 to 5,
        100 to 25
    )

    testCases.forEach { (a, b) ->
        println("Попытка деления: $a / $b")
        val result = safeDivide(a, b)
        result.onSuccess { value ->
            println("  Успех: $value")
        }.onFailure { error ->
            println("  Ошибка: ${error.message}")
        }
    }
}

// Задание 11 - пример
fun task11() {
    val sayHello = { println("Hello") }
    sayHello()

    val sayHelloExplicit: Function0<Unit> = object : Function0<Unit> {
        override fun invoke() {
            println("Hello from explicit")
        }
    }
    sayHelloExplicit.invoke()
}

fun main() {
    println("=== Задание 1 ===")
    task1()
    println("\n=== Задание 2 ===")
    task2()
    println("\n=== Задание 3 ===")
    task3()
    println("\n=== Задание 4 ===")
    task4()
    println("\n=== Задание 5 ===")
    task5()
    println("\n=== Задание 6 ===")
    task6()
    println("\n=== Задание 7 ===")
    task7()
    println("\n=== Задание 8 ===")
    task8()
    println("\n=== Задание 9 ===")
    task9()
    println("\n=== Задание 10 ===")
    task10()
    println("\n=== Задание 11 ===")
    task11()
}