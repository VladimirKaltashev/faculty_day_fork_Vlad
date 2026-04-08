package com.example.library.entity

import jakarta.persistence.*

// ============================================================================
// ЗАДАНИЕ ФИНАЛ: Создать сущность Reader
// ============================================================================
// ИНСТРУКЦИЯ:
// 1. Добавь @Entity и @Table
// 2. Добавь поля id, name, email

data class Reader(
     val email: String = ""
)
