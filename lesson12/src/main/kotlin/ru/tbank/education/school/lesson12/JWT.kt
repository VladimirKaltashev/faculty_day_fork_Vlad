package ru.tbank.education.school.lesson12

import java.net.HttpURLConnection
import java.net.URL
import java.security.cert.X509Certificate
import java.util.Base64
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun main() {
    disableSslVerification()

    val headerJson = """{"alg":"HS256","typ":"JWT"}"""
    val payloadJson = """{"user":"student","role":"student"}"""

    val secretKey = "my-secret-key"

    val encoder = Base64.getUrlEncoder().withoutPadding()
    val headerEncoded = encoder.encodeToString(headerJson.toByteArray())
    val payloadEncoded = encoder.encodeToString(payloadJson.toByteArray())

    val signature = createSignature("$headerEncoded.$payloadEncoded", secretKey)

    val jwt = "$headerEncoded.$payloadEncoded.$signature"

    println("JWT token:")
    println(jwt)
    println()

    val decoder = Base64.getUrlDecoder()

    val decodedHeader = String(decoder.decode(headerEncoded))
    val decodedPayload = String(decoder.decode(payloadEncoded))
    val decodedSignature = String(decoder.decode(signature)) // signature уже закодирован

    println("Decoded header:")
    println(decodedHeader)
    println()

    println("Decoded payload:")
    println(decodedPayload)
    println()

    println("Decoded signature (raw):")
    println(decodedSignature)
    println()

    println("=== Request WITH token ===")
    sendRequestWithBearer(jwt)

    println()

    println("=== Request WITHOUT token ===")
    sendRequestWithBearer(null)

    println()

    println("=== Request WITH modified payload ===")
    val tamperedPayloadJson = """{"user":"student","role":"admin"}"""
    val tamperedPayloadEncoded = encoder.encodeToString(tamperedPayloadJson.toByteArray())

    val tamperedJwt = "$headerEncoded.$tamperedPayloadEncoded.$signature"

    println("Tampered JWT (role: student → admin):")
    println(tamperedJwt)
    println()

    println("Decoded tampered payload:")
    println(String(decoder.decode(tamperedPayloadEncoded)))
    println()

    sendRequestWithBearer(tamperedJwt)
}

fun createSignature(data: String, secret: String): String {
    val mac = Mac.getInstance("HmacSHA256")
    val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
    mac.init(secretKey)
    val hash = mac.doFinal(data.toByteArray())
    return Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
}

fun sendRequestWithBearer(token: String?) {
    val url = URL("https://httpbin.org/bearer")
    val connection = url.openConnection() as HttpURLConnection

    connection.requestMethod = "GET"

    if (token != null) {
        connection.setRequestProperty("Authorization", "Bearer $token")
    }

    try {
        val statusCode = connection.responseCode
        println("Status code: $statusCode")

        val response = if (statusCode == 200) {
            connection.inputStream.bufferedReader().readText()
        } else {
            connection.errorStream?.bufferedReader()?.readText() ?: "No error stream"
        }

        println("Response: $response")

    } catch (e: Exception) {
        println("Error: ${e.message}")
    } finally {
        connection.disconnect()
    }
}

fun disableSslVerification() {
    val trustAll = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustAll, java.security.SecureRandom())
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
    HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
}