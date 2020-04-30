package com.tests.springsecurityreactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringSecurityReactiveApplication

fun main(args: Array<String>) {
	runApplication<SpringSecurityReactiveApplication>(*args)
}
