package ru.tbank.education.school.lesson12

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

// ===========================================
// Задача 6. Клиент для сервера заметок
// ===========================================
// Цель: написать клиент, который тестирует все эндпоинты сервера.
// Перед запуском: запустить Task6_Server.kt


val BASE = "http://localhost:8080/api/notes"

/** Отправить HTTP-запрос.
 *  @param url    — полный URL
 *  @param method — HTTP-метод
 *  @param body   — JSON-тело (null для GET/DELETE)
 *  @return Pair(statusCode, responseBody)
 */
fun request(url: String, method: String, body: String? = null): Pair<Int, String> {
    val connection = URL(url).openConnection() as HttpURLConnection

    connection.requestMethod = method
    connection.setRequestProperty("Content-Type", "application/json")
    connection.connectTimeout = 5000
    connection.readTimeout = 5000

    if (body != null) {
        connection.doOutput = true
        connection.outputStream.use {
            it.write(body.toByteArray())
        }
    }

    val statusCode = connection.responseCode

    val stream = if (statusCode in 200..299)
        connection.inputStream
    else
        connection.errorStream

    val response = stream?.bufferedReader()?.readText() ?: ""

    return Pair(statusCode, response)
}

fun main() {
    println("=== 1. GET /api/notes — все заметки ===")
    var response = request(BASE, "GET")
    println("Status: ${response.first}")
    println(response.second)

    println("\n=== 2. POST /api/notes — создать заметку ===")
    val createJson =
        """{"title":"Домашка","content":"Сделать задание по сетям","tag":"учёба"}"""

    response = request(BASE, "POST", createJson)
    println("Status: ${response.first}")
    println(response.second)


    println("\n=== 3. GET /api/notes/1 — одна заметка ===")
    response = request("$BASE/1", "GET")
    println("Status: ${response.first}")
    println(response.second)


    println("\n=== 4. PUT /api/notes/1 — обновить заметку ===")
    val updateJson =
        """{"title":"Покупки (обновлено)","content":"Молоко, хлеб, яйца, сыр","tag":"личное"}"""

    response = request("$BASE/1", "PUT", updateJson)
    println("Status: ${response.first}")
    println(response.second)


    println("\n=== 5. GET /api/notes?tag=учёба — фильтр по тегу ===")
    val tag = URLEncoder.encode("учёба", "UTF-8")
    response = request("$BASE?tag=$tag", "GET")
    println("Status: ${response.first}")
    println(response.second)


    println("\n=== 6. DELETE /api/notes/1 — удалить заметку ===")
    response = request("$BASE/1", "DELETE")
    println("Status: ${response.first}")
    println(response.second)


    println("\n=== 7. GET /api/notes/999 — несуществующая заметка ===")
    response = request("$BASE/999", "GET")
    println("Status: ${response.first}")
    println(response.second)


    println("\n=== 8. GET /api/notes — финальное состояние ===")
    response = request(BASE, "GET")
    println("Status: ${response.first}")
    println(response.second)
}