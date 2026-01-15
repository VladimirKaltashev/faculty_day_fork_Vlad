package ru.tbank.education.school.lesson2.Sport

// 1. Базовый интерфейс для любой активности, которую можно измерить
interface MeasurableActivity {
    val duration: Int // в минутах
    val intensity: Int // 1-10 баллов
    val date: java.time.LocalDate

    fun calculateCaloriesBurned(): Double // абстрактный расчет
    fun getSummary(): String // описание тренировки
}

// 2. Интерфейс для сущностей, у которых есть прогресс и цель
interface Achievable {
    val targetValue: Double
    val currentValue: Double
    val deadline: java.time.LocalDate?

    fun getProgressPercentage(): Double
    fun isOnTrack(): Boolean
    fun updateProgress(newValue: Double)
}

// 3. Интерфейс для всего, что требует восстановления
interface RecoveryAware {
    val recoveryTimeHours: Int
    var lastSessionDate: java.time.LocalDate?

    fun isFullyRecovered(): Boolean
    fun getRecoveryPercentage(): Double
}

// 4. Интерфейс для соревнований
interface Competition {
    val name: String
    val date: java.time.LocalDate
    val priority: Priority // enum класс HIGH, MEDIUM, LOW

    fun daysUntil(): Int
    fun getPreparationStatus(): String
}

// 5. Интерфейс для стратегии расчета нагрузки
interface LoadCalculationStrategy {
    fun calculateTrainingLoad(activities: List<MeasurableActivity>): Double
    fun recommendRecoveryDays(load: Double): Int
}

// Пример enum для приоритета
enum class Priority {
    HIGH, MEDIUM, LOW
}