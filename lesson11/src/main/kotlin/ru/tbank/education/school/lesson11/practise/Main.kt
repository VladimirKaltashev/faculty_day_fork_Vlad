package ru.tbank.education.school.lesson11.practise

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern
import java.time.temporal.ChronoUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.*
import java.math.BigInteger
import kotlinx.coroutines.*
import java.io.File
import kotlinx.coroutines.CancellationException

fun main() {
    val threads = CreateThreads.run()
    threads.forEach { it.join() }

    print(RaceCondition.run())

    println()

    print(RaceCondition.syncRun())

    println()

    print(SynchronizedCounter.run())

    println()

    Deadlock.runDeadlock()

    Thread.sleep(2000)
    println("\nFixed version")
    Deadlock.runFixed()
    println()
    ExecutorServiceExample.run()
    println()
    val factorials = FutureFactorial.run()
    factorials.forEach { (num, fact) ->
        println("$num! = $fact")
    }

    println()
    CoroutineLaunch.run()

    println()
    val sum = AsyncAwait.run()
    println("Sum from 1 to 1,000,000 = $sum")

    println()
    try {
        StructuredConcurrency.run(failingCoroutineIndex = 2)
    } catch (e: Exception) {
        println("Caught exception as expected: ${e.message}")
    }

    println()

    createTestFiles()
    val results = WithContextIO.run(listOf("test1.txt", "test2.txt", "test3.txt"))
    results.forEach { (file, content) ->
        println("$file: $content")
    }

    cleanupTestFiles()
}

// Часть 1. Потоки (Thread)

/* ### Задание 1. Создание потоков
Создайте 3 потока с именами "Thread-A", "Thread-B", "Thread-C". Каждый поток должен вывести своё имя 5 раз с задержкой 500мс.
*/

object CreateThreads {
    fun run(): List<Thread> {
        val threadNames = listOf("Thread-A", "Thread-B", "Thread-C")
        val threads = threadNames.map { name ->
            Thread {
                repeat(5) {
                    println("$name: iteration ${it + 1}")
                    try {
                        Thread.sleep(500)
                    } catch (e: InterruptedException) {
                        println("$name was interrupted")
                        return@Thread
                    }
                }
            }.apply {
                this.name = name
            }
        }

        threads.forEach { it.start() }
        return threads
    }
}

/* Задание 2. Race condition
Создайте переменную `counter = 0`. Запустите 10 потоков,
каждый из которых увеличивает counter на 1000.
Выведите финальное значение и объясните результат.
*/
object RaceCondition {
    fun run(): Int {
        var counter = 0

        val threads = (1..10).map {
            Thread {
                repeat(1000) {
                    counter++

                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }
        return counter
    }

    fun syncRun(): Int {
        var counter = AtomicInteger(0)

        val threads = (1..10).map {
            Thread {
                repeat(1000) {
                    counter.incrementAndGet()
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }
        return counter.toInt()
    }
}

/* Задание 3. Synchronized
Исправьте задание 2 с помощью `@Synchronized` или `synchronized {}` блока, чтобы результат всегда был 10000.
*/
object SynchronizedCounter {
    fun run(): Int {
        var counter = 0
        val lock = Any()

        val threads = (1..10).map {
            Thread {
                repeat(1000) {
                    synchronized(lock) {
                        counter++
                    }
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }
        return counter
    }
}

/* Задание 4. Deadlock
Создайте пример deadlock с двумя ресурсами и двумя потоками. Затем исправьте его.
*/

object Deadlock {
    fun runDeadlock() {
        val resource1 = Any()
        val resource2 = Any()

        val thread1 = Thread {
            synchronized(resource1) {
                println("Thread 1 locked resource 1")
                Thread.sleep(100)
                println("Thread 1 waiting for resource 2")
                synchronized(resource2) {
                    println("Thread 1 locked resource 2")
                }
            }
        }

        val thread2 = Thread {
            synchronized(resource2) {
                println("Thread 2 locked resource 2")
                Thread.sleep(100)
                println("Thread 2 waiting for resource 1")
                synchronized(resource1) {
                    println("Thread 2 locked resource 1")
                }
            }
        }

        thread1.start()
        thread2.start()

        Thread.sleep(1000)
        println("Deadlock occurred! Program might hang here...")
    }

    fun runFixed(): Boolean {
        val resource1 = Any()
        val resource2 = Any()

        val thread1 = Thread {
            synchronized(resource1) {
                println("Thread 1 locked resource 1")
                Thread.sleep(100)
                println("Thread 1 waiting for resource 2")
                synchronized(resource2) {
                    println("Thread 1 locked resource 2")
                }
            }
        }

        val thread2 = Thread {
            synchronized(resource1) {
                println("Thread 2 locked resource 1")
                Thread.sleep(100)
                println("Thread 2 waiting for resource 2")
                synchronized(resource2) {
                    println("Thread 2 locked resource 2")
                }
            }
        }

        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()

        println("Fixed version completed without deadlock!")
        return true
    }
}

// Часть 2. Executor Framework

/* ### Задание 5. ExecutorService
Используя `Executors.newFixedThreadPool(4)`, выполните 20 задач. Каждая задача выводит свой номер и имя потока, затем спит 200мс.
*/

object ExecutorServiceExample {
    fun run(): List<String> {
        val executor = Executors.newFixedThreadPool(4)
        val results = mutableListOf<String>()

        val tasks = (1..20).map { taskId ->
            Callable {
                val threadName = Thread.currentThread().name
                val message = "Task $taskId running on $threadName"
                println(message)
                Thread.sleep(200)
                message
            }
        }

        try {
            val futures = executor.invokeAll(tasks)
            futures.forEach { future ->
                results.add(future.get())
            }
        } finally {
            executor.shutdown()
            executor.awaitTermination(10, TimeUnit.SECONDS)
        }

        return results
    }
}

/* ### Задание 6. Future
Используя ExecutorService и `Callable`, параллельно вычислите факториалы чисел от 1 до 10.
Соберите результаты через `Future.get()`.
*/

object FutureFactorial {
    fun run(): Map<Int, BigInteger> {
        val executor = Executors.newFixedThreadPool(4)
        val futures = mutableListOf<Future<Pair<Int, BigInteger>>>()
        val results = mutableMapOf<Int, BigInteger>()

        try {
            // Создаем задачи для каждого числа
            for (i in 1..10) {
                val future = executor.submit(Callable {
                    Pair(i, factorial(i))
                })
                futures.add(future)
            }

            // Собираем результаты
            futures.forEach { future ->
                val (num, fact) = future.get()
                results[num] = fact
            }

        } finally {
            executor.shutdown()
            executor.awaitTermination(10, TimeUnit.SECONDS)
        }

        return results.toSortedMap()
    }

    private fun factorial(n: Int): BigInteger {
        return (1..n).fold(BigInteger.ONE) { acc, i ->
            acc.multiply(BigInteger.valueOf(i.toLong()))
        }
    }
}

// Часть 3. Корутины

/* ### Задание 7. Первая корутина
Используя `runBlocking` и `launch`, запустите 3 корутины, каждая из которых выводит своё имя 5 раз с `delay(500)`.
*/

object CoroutineLaunch {
    fun run(): List<String> = runBlocking {
        val results = mutableListOf<String>()
        val coroutineNames = listOf("Coroutine-A", "Coroutine-B", "Coroutine-C")

        val jobs = coroutineNames.map { name ->
            launch {
                repeat(5) { index ->
                    val message = "$name: iteration ${index + 1} on ${Thread.currentThread().name}"
                    println(message)
                    results.add(message)
                    delay(500)
                }
            }
        }

        jobs.forEach { it.join() }
        return@runBlocking results
    }
}

/* ### Задание 8. async/await
Используя `async`, параллельно вычислите сумму чисел от 1 до 1_000_000, разбив на 4 части.
Соберите результаты через `await()`.
*/

object AsyncAwait {
    fun run(): Long = runBlocking {
        val total = 1_000_000L
        val chunkSize = total / 4

        val deferreds = (0..3).map { index ->
            async {
                val start = index * chunkSize + 1
                val end = if (index == 3) total else (index + 1) * chunkSize
                (start..end).sum()
            }
        }

        deferreds.sumOf { it.await() }
    }
}

/* ### Задание 9. Structured concurrency
Создайте корутину, которая запускает 5 дочерних корутин.
Если одна из них падает с исключением, все остальные должны отмениться.
*/

object StructuredConcurrency {
    fun run(failingCoroutineIndex: Int): Int = runBlocking {
        var completedCount = 0

        try {
            coroutineScope {
                val jobs = (0..4).map { index ->
                    launch {
                        try {
                            println("Child $index started")

                            repeat(5) { step ->
                                if (index == failingCoroutineIndex && step == 2) {
                                    throw RuntimeException("Child $index failed intentionally")
                                }
                                delay(100)
                                println("Child $index step $step completed")
                            }

                            completedCount++
                            println("Child $index completed successfully")
                        } catch (e: CancellationException) {
                            println("Child $index was cancelled")
                            throw e
                        }
                    }
                }

                jobs.forEach { it.join() }
            }
        } catch (e: Exception) {
            println("Caught exception in parent scope: ${e.message}")
        }

        // Даем время на отмену
        delay(500)
        println("Completed count: $completedCount (should be less than 5 if one failed)")
        return@runBlocking completedCount
    }
}

/* ### Задание 10. withContext
Используя `withContext(Dispatchers.IO)`, прочитайте содержимое 3 файлов параллельно и объедините результаты.
*/

object WithContextIO {
    fun run(filePaths: List<String>): Map<String, String> = runBlocking {
        val deferreds = filePaths.map { filePath ->
            async {
                filePath to readFileContent(filePath)
            }
        }

        deferreds.awaitAll().toMap()
    }

    private suspend fun readFileContent(filePath: String): String = withContext(Dispatchers.IO) {
        try {
            File(filePath).readText()
        } catch (e: Exception) {
            "Error reading file: ${e.message}"
        }
    }
}

// Вспомогательная функция для создания тестовых файлов
private fun createTestFiles() {
    listOf(
        "test1.txt" to "Content of file 1",
        "test2.txt" to "Content of file 2",
        "test3.txt" to "Content of file 3"
    ).forEach { (fileName, content) ->
        File(fileName).writeText(content)
    }
}

private fun cleanupTestFiles() {
    listOf("test1.txt", "test2.txt", "test3.txt").forEach { fileName ->
        File(fileName).delete()
    }
}