package com.example.library.repository

// ============================================================================
// ЗАДАНИЕ ФИНАЛ: Создать интерфейс ReaderRepository
// ============================================================================
// ИНСТРУКЦИЯ:
// 2. Создай интерфейс

import com.example.library.entity.Reader
import org.springframework.data.jpa.repository.JpaRepository

interface ReaderRepository : JpaRepository<Reader, Long>
