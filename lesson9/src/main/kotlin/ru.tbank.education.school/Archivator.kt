package ru.tbank.education.school

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class CustomArchiver {
    fun createArchive(sourceDirPath: String, archivePath: String, extensions: List<String>? = null) {
        val sourceDir = File(sourceDirPath)
        if (!sourceDir.exists() || !sourceDir.isDirectory) {
            println("Error: $sourceDirPath does not exist or is not a directory")
            return
        }

        val fileOutputStream = FileOutputStream(archivePath)
        val zipOutputStream = ZipOutputStream(fileOutputStream)

        try {
            val filesToArchive = getAllFiles(sourceDir, extensions)

            if (filesToArchive.isEmpty()) {
                println("No files to archive")
                return
            }

            println("Starting archiving")
            println("Number of files to archive: ${filesToArchive.size}")
            println("----------------------------------------")

            for (file in filesToArchive) {
                addFileToZip(file, sourceDir, zipOutputStream)
            }

            println("----------------------------------------")
            println("Archiving completed successfully!")
            println("Archive saved: $archivePath")
        } catch (e: Exception) {
            println("Error while creating archive: ${e.message}")
            e.printStackTrace()
        } finally {
            try {
                zipOutputStream.close()
                fileOutputStream.close()
            } catch (e: Exception) {
                println("Error while closing streams: ${e.message}")
            }
        }
    }

    private fun getAllFiles(directory: File, extensions: List<String>?): List<File> {
        val allFiles = mutableListOf<File>()

        directory.walk().forEach { file ->
            if (file.isFile) {
                if (extensions == null || hasValidExtension(file, extensions)) {
                    allFiles.add(file)
                }
            }
        }

        return allFiles
    }

    private fun hasValidExtension(file: File, extensions: List<String>): Boolean {
        val fileName = file.name
        val dotIndex = fileName.lastIndexOf(".")

        if (dotIndex == -1) return false // file without extension

        val extension = fileName.substring(dotIndex + 1).lowercase()
        return extensions.any { it.lowercase() == extension }
    }

    private fun addFileToZip(file: File, baseDir: File, zipOutputStream: ZipOutputStream) {
        var fileInputStream: FileInputStream? = null

        try {
            val relativePath = file.relativeTo(baseDir).path
            val zipEntry = ZipEntry(relativePath)

            zipOutputStream.putNextEntry(zipEntry)

            fileInputStream = FileInputStream(file)

            val buffer = ByteArray(1024)
            var bytesRead: Int

            var totalBytes = 0
            while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                zipOutputStream.write(buffer, 0, bytesRead)
                totalBytes += bytesRead
            }

            zipOutputStream.closeEntry()

            println("File added: $relativePath")
            println("  Size: ${totalBytes} bytes (${String.format("%.2f", totalBytes / 1024.0)} KB)")

        } catch (e: Exception) {
            println("Error while adding file ${file.name}: ${e.message}")
        } finally {
            fileInputStream?.close()
        }
    }
}