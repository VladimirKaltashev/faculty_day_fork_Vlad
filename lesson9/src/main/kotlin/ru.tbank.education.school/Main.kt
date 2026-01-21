package ru.tbank.education.school

fun main() {
    val archiver = CustomArchiver()

    val sourceDirectory = "data_to_archive"
    val archiveName = "my_archive.zip"
    val allowedExtensions = listOf("txt", "log", "csv")

    archiver.createArchive(sourceDirectory, archiveName, allowedExtensions)

    println("\n" + "=".repeat(50))
    println("Task completed!")
    println("Check the created archive: $archiveName")
}