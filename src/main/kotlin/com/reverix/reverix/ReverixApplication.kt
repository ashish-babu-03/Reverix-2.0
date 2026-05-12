package com.reverix.reverix

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ReverixApplication

fun main(args: Array<String>) {
	runApplication<ReverixApplication>(*args)
}
