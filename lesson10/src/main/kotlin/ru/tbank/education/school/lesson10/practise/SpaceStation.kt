package ru.tbank.education.school.lesson10.practise

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.regex.Pattern

data class LogRecord(
    val dt: String,
    val id: Int,
    val status: String
)

fun checkSchedule() {
    val logs = listOf(
        "2026-01-22 09:14 | ID:042 | STATUS:sent",
        "TS=22/01/2026-09:27; status=delivered; #042",
        "2026-01-22 09:10 | ID:043 | STATUS:sent",
        "2026-01-22 09:18 | ID:043 | STATUS:delivered",
        "TS=22/01/2026-09:05; status=sent; #044",
        "[22.01.2026 09:40] delivered (id:044)",
        "2026-01-22 09:20 | ID:045 | STATUS:sent",
        "[22.01.2026 09:33] delivered (id:045)",
        "   ts=22/01/2026-09:50; STATUS=Sent; #046   ",
        " [22.01.2026 10:05]   DELIVERED   (ID:046) "
    )

    // Часть 1: Нормализация логов

    val patternA = Pattern.compile("""^(\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2})\s*\|\s*ID:(\d+)\s*\|\s*STATUS:(\w+)$""", Pattern.CASE_INSENSITIVE)
    val patternB = Pattern.compile("""^TS=(\d{2}/\d{2}/\d{4})-(\d{2}:\d{2});\s*status=(\w+);\s*#(\d+)$""", Pattern.CASE_INSENSITIVE)
    val patternC = Pattern.compile("""^\[(\d{2}\.\d{2}\.\d{4})\s+(\d{2}:\d{2})\]\s+(\w+)\s*\(id:(\d+)\)$""", Pattern.CASE_INSENSITIVE)

    fun normalize(line: String): LogRecord? {
        val trimmedLine = line.trim()

        var matcher = patternA.matcher(trimmedLine)
        if (matcher.find()) {
            val dt = matcher.group(1) // уже в правильном формате
            val id = matcher.group(2).toInt()
            val status = matcher.group(3).lowercase()
            if (status != "sent" && status != "delivered") return null
            return LogRecord(dt, id, status)
        }

        matcher = patternB.matcher(trimmedLine)
        if (matcher.find()) {
            val datePart = matcher.group(1) // DD/MM/YYYY
            val timePart = matcher.group(2) // HH:MM
            val status = matcher.group(3).lowercase()
            val id = matcher.group(4).toInt()
            if (status != "sent" && status != "delivered") return null

            val dt = "${datePart.substring(6, 10)}-${datePart.substring(3, 5)}-${datePart.substring(0, 2)} $timePart"
            return LogRecord(dt, id, status)
        }

        matcher = patternC.matcher(trimmedLine)
        if (matcher.find()) {
            val datePart = matcher.group(1) // DD.MM.YYYY
            val timePart = matcher.group(2) // HH:MM
            val status = matcher.group(3).lowercase()
            val id = matcher.group(4).toInt()
            if (status != "sent" && status != "delivered") return null

            val dt = "${datePart.substring(6, 10)}-${datePart.substring(3, 5)}-${datePart.substring(0, 2)} $timePart"
            return LogRecord(dt, id, status)
        }

        return null
    }

    val normalized = mutableListOf<LogRecord>()
    val broken = mutableListOf<String>()

    for (log in logs) {
        val record = normalize(log)
        if (record != null) {
            normalized.add(record)
        } else {
            broken.add(log)
        }
    }

    println("=== Часть 1: Нормализация логов ===")
    println("Нормализованные записи (${normalized.size}):")
    normalized.forEach { println("  $it") }
    if (broken.isNotEmpty()) {
        println("\nБитые строки (${broken.size}):")
        broken.forEach { println("  '$it'") }
    }

    // Часть 2: Расчёт времени доставки

    val groupedById = normalized.groupBy { it.id }

    val deliveryTimes = mutableListOf<Pair<Int, Long>>()
    val incomplete = mutableListOf<Int>()
    val timeErrors = mutableListOf<Int>()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    for ((id, records) in groupedById) {
        val sentRecord = records.find { it.status == "sent" }
        val deliveredRecord = records.find { it.status == "delivered" }

        if (sentRecord == null || deliveredRecord == null) {
            incomplete.add(id)
            continue
        }

        val sentTime = LocalDateTime.parse(sentRecord.dt, formatter)
        val deliveredTime = LocalDateTime.parse(deliveredRecord.dt, formatter)

        if (deliveredTime.isBefore(sentTime)) {
            timeErrors.add(id)
            continue
        }

        val duration = ChronoUnit.MINUTES.between(sentTime, deliveredTime)
        deliveryTimes.add(id to duration)
    }

    println("\n=== Часть 2: Результаты расчёта времени ===")
    if (incomplete.isNotEmpty()) {
        println("Неполные заказы (нет sent или delivered): $incomplete")
    }
    if (timeErrors.isNotEmpty()) {
        println("Ошибки времени (delivered раньше sent): $timeErrors")
    }

    // Часть 3: Отчёт

    val sortedDeliveryTimes = deliveryTimes.sortedByDescending { it.second }

    println("\n=== Часть 3: Отчёт ===")
    println("ID с длительностью доставки (отсортировано по убыванию):")
    sortedDeliveryTimes.forEach { (id, minutes) ->
        println("  ID $id: $minutes минут")
    }

    if (sortedDeliveryTimes.isNotEmpty()) {
        val longest = sortedDeliveryTimes.first()
        println("\nСамый долгий заказ:")
        println("  ID ${longest.first}: ${longest.second} минут")
    }

    val violators = sortedDeliveryTimes.filter { it.second > 20 }
    if (violators.isNotEmpty()) {
        println("\nНарушители правила (доставка > 20 минут):")
        violators.forEach { (id, minutes) ->
            println("  ID $id: $minutes минут")
        }
    } else {
        println("\nНарушителей правила (доставка > 20 минут) нет.")
    }
}

fun main() {
    checkSchedule()
}