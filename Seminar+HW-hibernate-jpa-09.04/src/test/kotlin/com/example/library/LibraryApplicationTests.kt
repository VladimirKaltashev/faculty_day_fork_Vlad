package com.example.library

import com.example.library.service.LibraryService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class LibraryApplicationTests {

    @Autowired
    lateinit var libraryService: LibraryService

    @Test
    fun contextLoads() {
        assertNotNull(libraryService)
    }
}
