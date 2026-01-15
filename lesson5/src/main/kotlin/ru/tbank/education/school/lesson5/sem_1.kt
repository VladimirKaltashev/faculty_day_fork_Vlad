package ru.tbank.education.school.lesson5

fun main() {
    // Исходный список продуктов
    val products = listOf("Молоко", "Хлеб", "Сахар", "Сыр", "Масло", "Колбаса", "Сметана", "Яблоки")

    // todo 1. Проверка наличия "Хлеб" в коллекции

    if (products.contains("Хлеб")) {
        println("Среди продуктов есть хлеб")
    } else {
        println("Среди продуктов нет хлеба")
    }

    // todo 2. Сортировка по алфавиту и вывод

    val sortedProducts = products.sorted()
    println(sortedProducts)

    // todo 3. Вывод только продуктов, начинающихся на букву "С"

    for (product in products) {
        if (product[0] == 'С') {
            print(product + ' ')
        }
    }
}