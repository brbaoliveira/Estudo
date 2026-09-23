package com.kontrol.api.config
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HealthController {
    @GetMapping("/health")
    fun health() {
        mapOf("status" to "UP", "service" to "KontrolAPI")
    }
}
