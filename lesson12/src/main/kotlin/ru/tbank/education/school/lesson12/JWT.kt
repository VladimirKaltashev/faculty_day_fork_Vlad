package ru.tbank.education.school.lesson12

import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64

fun main() {
    val headerJson = """{"alg":"HS256","typ":"JWT"}"""
    val payloadJson = """{"user":"student","role":"student"}"""

    val signature = "mysignature"

    val encoder = Base64.getUrlEncoder().withoutPadding()
    val headerEncoded =
        encoder.encodeToString(headerJson.toByteArray())
    val payloadEncoded =
        encoder.encodeToString(payloadJson.toByteArray())
    val signatureEncoded =
        encoder.encodeToString(signature.toByteArray())

    val jwt = "$headerEncoded.$payloadEncoded.$signatureEncoded"

    println("JWT token:")
    println(jwt)

    println()

    val decoder = Base64.getUrlDecoder()

    val decodedHeader =
        String(decoder.decode(headerEncoded))

    val decodedPayload =
        String(decoder.decode(payloadEncoded))

    println("Decoded header:")
    println(decodedHeader)

    println()

    println("Decoded payload:")
    println(decodedPayload)

    println()

    val url = URL("https://httpbin.org/bearer")

    val connection = url.openConnection() as HttpURLConnection

    connection.requestMethod = "GET"

    connection.setRequestProperty(
        "Authorization",
        "Bearer $jwt"
    )

    val statusWithToken = connection.responseCode

    println("Status WITH token: $statusWithToken")

    val responseWithToken =
        connection.inputStream.bufferedReader().readText()

    println(responseWithToken)

    println()

    val connection2 = url.openConnection() as HttpURLConnection

    connection2.requestMethod = "GET"

    val statusWithoutToken = connection2.responseCode

    println("Status WITHOUT token: $statusWithoutToken")

}