package ru.tbank.education.school.lesson2.Sport

interface MeasurableActivity {
    val duration: Int // в минутах
    val intensity: Int // 1-10 баллов
    val date: java.time.LocalDate

    fun calculateCaloriesBurned(): Double // абстрактный расчет
    fun getSummary(): String // описание тренировки
}