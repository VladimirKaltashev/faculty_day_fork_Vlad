package ru.tbank.education.school.lesson11.practise

import java.io.File
import java.net.URL
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger

data class DownloadStats(
    val totalTimeMs: Long,
    val successful: Int,
    val failed: Int,
    val total: Int
)

object ImageDownloader {
    fun run(urls: List<String>, outputDir: String): DownloadStats = runBlocking {
        val startTime = System.currentTimeMillis()

        // Создаем папку для загрузок
        File(outputDir).mkdirs()

        val completedCount = AtomicInteger(0)
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)
        val mutex = Mutex()

        // Запускаем загрузку параллельно
        val jobs = urls.mapIndexed { index, url ->
            async(Dispatchers.IO) {
                try {
                    downloadImage(url, outputDir, index + 1)
                    successCount.incrementAndGet()
                } catch (e: Exception) {
                    failCount.incrementAndGet()
                    mutex.withLock {
                        println("Ошибка загрузки ${url.take(50)}...: ${e.message}")
                    }
                } finally {
                    val completed = completedCount.incrementAndGet()
                    // Синхронизируем
                    mutex.withLock {
                        println("Загружено $completed/${urls.size}")
                    }
                }
            }
        }

        // Ждем завершения
        jobs.joinAll()

        val totalTime = System.currentTimeMillis() - startTime

        return@runBlocking DownloadStats(
            totalTimeMs = totalTime,
            successful = successCount.get(),
            failed = failCount.get(),
            total = urls.size
        )
    }

    private suspend fun downloadImage(urlString: String, outputDir: String, index: Int) {
        withContext(Dispatchers.IO) {
            val url = URL(urlString)
            val fileName = "image_$index.jpg"
            val outputFile = File(outputDir, fileName)

            url.openStream().use { input ->
                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}

fun main() = runBlocking {
    println("---Задание 11: Загрузчик изображений---\n")

    // Создаем 10 URL для загрузки
    val urls = List(10) { "https://picsum.photos/200/300" }
    val outputDir = "downloads"

    println("Начинаем загрузку ${urls.size} изображений\n")

    val stats = ImageDownloader.run(urls, outputDir)

    println("\n---Статистика загрузки---")
    println("Всего времени: ${stats.totalTimeMs} мс")
    println("Успешно: ${stats.successful}")
    println("Неудачно: ${stats.failed}")
    println("Всего: ${stats.total}")

    // Показываем список загруженных файлов
    val downloadedFiles = File(outputDir).listFiles()
    if (downloadedFiles != null && downloadedFiles.isNotEmpty()) {
        println("\nЗагруженные файлы (${downloadedFiles.size}):")
        downloadedFiles.forEach { file ->
            println("  - ${file.name} (${file.length()} байт)")
        }
    }
}