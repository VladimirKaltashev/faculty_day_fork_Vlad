package ru.tbank.education.school.lesson10.practise
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern
import java.time.temporal.ChronoUnit

fun main() {
    task1()
    println()
    task2()
    println()
    task3()
    println()
    task4()
    println()
    task5()
    println()
    task6()
    println()
    task7()
    println()
    task8()
}

/*
1) Строки + регулярные выражения
["Name: Ivan, score=17", ...]
Извлечь имя и score, собрать пары, вывести победителя.
*/
fun task1() {
    val lines = listOf(
        "Name: Ivan, score=17",
        "Name: Olga, score=23",
        "Name: Max, score=5"
    )

    val re = Regex("""^Name:\s*([A-Za-z]+)\s*,\s*score=(\d+)\s*$""")

    val pairs: List<Pair<String, Int>> = lines.mapNotNull { s ->
        val m = re.find(s) ?: return@mapNotNull null
        val name = m.groupValues[1]
        val score = m.groupValues[2].toInt()
        name to score
    }

    println("Task 1 pairs: $pairs")

    val best = pairs.maxByOrNull { it.second }
    if (best != null) {
        println("Task 1 best: ${best.first} (${best.second})")
    } else {
        println("Task 1: no valid lines")
    }
}

/*
2) Даты + коллекции
["2026-01-22", ...]
Преобразовать в даты, отсортировать, посчитать сколько в январе 2026.
*/
fun task2() {
    val dateStrings = listOf(
        "2026-01-22",
        "2026-02-01",
        "2025-12-31",
        "2026-01-05"
    )

    val fmt = DateTimeFormatter.ISO_LOCAL_DATE

    val dates = dateStrings.map { LocalDate.parse(it, fmt) }.sorted()

    println("Task 2 sorted dates: ${dates.joinToString { it.format(fmt) }}")

    val countJan2026 = dates.count { it.year == 2026 && it.month == Month.JANUARY }
    println("Task 2 count in Jan 2026: $countJan2026")
}

/*
3) Коллекции + строки
"apple orange apple banana orange apple"
Частоты слов, вывести слова с частотой > 1 по алфавиту.
*/
fun task3() {
    val text = "apple orange apple banana orange apple"

    val words = text.trim().split(Regex("""\s+""")).filter { it.isNotEmpty() }

    val freq = mutableMapOf<String, Int>()
    for (w in words) {
        freq[w] = (freq[w] ?: 0) + 1
    }

    println("Task 3 freq: $freq")

    val repeated = freq
        .filter { (_, c) -> c > 1 }
        .keys
        .sorted()

    println("Task 3 repeated words: ${repeated.joinToString(", ")}")
}

/*
4) Регулярные выражения: проверка формата
["A-123", "B-7", "AA-12", "C-001", "D-99x"]
Задача: оставить только строки формата: **одна заглавная буква**, затем `-`, затем **1–3 цифры**. Вывести отфильтрованный список.
 */
fun task4() {
    val strings = listOf("A-123", "B-7", "AA-12", "C-001", "D-99x")

    val pattern = Pattern.compile("^[A-Z]-\\d{1,3}$")

    val filteredStrings = strings.filter { str ->
        pattern.matcher(str).matches()
    }

    println("Task 4 filtered array: $filteredStrings")
}

/*
5) Строки: нормализация пробелов
["  Hello   world  ", "A   B    C", "   one"]

Задача: для каждой строки убрать пробелы по краям и заменить подряд идущие пробелы внутри на один пробел.
Вывести результат.
 */
fun task5() {
    val strings = listOf("  Hello   world  ", "A   B    C", "   one")
    val trimmedStrings = strings.map { str ->
        str.trim()
    }
    println("Task 5 trimmed strings: $trimmedStrings")
}

/*
6) Даты: разница между двумя датами
[("2026-01-01","2026-01-10"), ("2025-12-31","2026-01-01"), ("2026-02-01","2026-01-22")]
Задача: для каждой пары посчитать разницу в днях (вторая - первая) и вывести список чисел.
*/
fun task6() {
    val datePairs = listOf(
        "2026-01-01" to "2026-01-10",
        "2025-12-31" to "2026-01-01",
        "2026-02-01" to "2026-01-22"
    )

    val differences = datePairs.map { (firstStr, secondStr) ->
        val firstDate = LocalDate.parse(firstStr)
        val secondDate = LocalDate.parse(secondStr)
        ChronoUnit.DAYS.between(firstDate, secondDate)
    }

    println("Task 6 date differences: $differences")
}

/*
7) Коллекции: группировка по ключу
Дан список строк:
["math:Ivan", "bio:Olga", "math:Max", "bio:Ivan", "cs:Olga"]
Задача: построить словарь вида предмет -> список учеников, сохранив порядок появления учеников внутри предмета. Вывести словарь.
*/
fun task7() {
    val lines = listOf(
        "math:Ivan",
        "bio:Olga",
        "math:Max",
        "bio:Ivan",
        "cs:Olga"
    )

    val groups = mutableMapOf<String, MutableList<String>>()

    for (line in lines) {
        val parts = line.split(":")
        if (parts.size != 2) continue

        val subject = parts[0]
        val student = parts[1]

        val list = groups.getOrPut(subject) { mutableListOf() }
        if (!list.contains(student)) {
            list.add(student)
        }
    }

    println("Task 7 groups: $groups")
}

/*
8) Регулярные выражения + даты: извлечение времени из текста
Дан список строк:
["Start at 2026/01/22 09:14", "No time here", "End: 22-01-2026 18:05"]
Задача: найти строки, где есть дата и время, извлечь их и привести к формату "YYYY-MM-DD HH:MM". Строки без времени игнорировать.
*/
fun task8() {
    val strings = listOf(
        "Start at 2026/01/22 09:14",
        "No time here",
        "End: 22-01-2026 18:05"
    )

    val pattern = Pattern.compile("""\b(\d{4}/\d{2}/\d{2} \d{2}:\d{2})\b|\b(\d{2}-\d{2}-\d{4} \d{2}:\d{2})\b""")

    val results = strings.mapNotNull { str ->
        val matcher = pattern.matcher(str)
        if (matcher.find()) {
            val found = matcher.group(1) ?: matcher.group(2)

            val parsedDateTime = if (found!!.contains("/")) {
                val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
                LocalDateTime.parse(found, formatter)
            } else {
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                LocalDateTime.parse(found, formatter)
            }

            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            parsedDateTime.format(outputFormatter)
        } else {
            null
        }
    }

    println("Task 8 extracted dates: $results")
}