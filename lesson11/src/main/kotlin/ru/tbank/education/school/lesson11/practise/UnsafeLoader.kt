package homework

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger

suspend fun main() {
    val coroutineCount = 20
    val incrementsPerCoroutine = 1000
    val expectedValue = coroutineCount * incrementsPerCoroutine
    println()
    println("=== Тестирование гонки данных ===")
    println("Параметры: 20 корутин × 1000 инкрементов")
    println("Ожидаемый результат: 20000\n")

    // UnsafeCounter
    println("1. Запуск UnsafeCounter (с ошибкой)")
    val unsafeCounter = UnsafeCounter()
    val unsafeResult = unsafeCounter.runConcurrentIncrements(coroutineCount, incrementsPerCoroutine)

    if (unsafeResult == expectedValue) {
        println("   Результат: $unsafeResult (Гонки не произошло)")
    } else {
        println("   Результат: $unsafeResult")
        println("   Потеряно значений: ${expectedValue - unsafeResult}")
    }

    println()

    // SafeCounter
    println("2. Запуск SafeCounter (исправленный)")
    val safeCounter = SafeCounter()
    val safeResult = safeCounter.runConcurrentIncrements(coroutineCount, incrementsPerCoroutine)

    if (safeResult == expectedValue) {
        println("   Результат: $safeResult (Все инкременты учтены!)")
    } else {
        println("   Результат: $safeResult (Что-то пошло не так)")
    }
}

/**
 *
 * Задание: Исправьте гонку данных в этом классе любым из известных вам способов
 *
 * Проблема: Несколько корутин одновременно увеличивают счетчик `value`,
 * что приводит к потере некоторых инкрементов из-за race condition.
 */
class UnsafeCounter {

    private var value = 0

    suspend fun increment() {
        delay(1)
        value++
    }

    fun getValue(): Int = value

    suspend fun runConcurrentIncrements(
        coroutineCount: Int = 10,
        incrementsPerCoroutine: Int = 1000
    ): Int = coroutineScope {

        val jobs = List(coroutineCount) {
            launch(Dispatchers.Default) {
                repeat(incrementsPerCoroutine) {
                    increment()
                }
            }
        }

        jobs.joinAll()

        getValue()
    }
}


// Исправленная версия
class SafeCounter {

    private var value = 0
    private val mutex = Mutex()

    suspend fun increment() {
        delay(1)
        // Блокируем доступ пока выполняется инкремент
        mutex.withLock {
            value++
        }
    }

    fun getValue(): Int = value

    suspend fun runConcurrentIncrements(
        coroutineCount: Int = 10,
        incrementsPerCoroutine: Int = 1000
    ): Int = coroutineScope {

        val jobs = List(coroutineCount) {
            launch(Dispatchers.Default) {
                repeat(incrementsPerCoroutine) {
                    increment()
                }
            }
        }

        jobs.joinAll()

        getValue()
    }
}