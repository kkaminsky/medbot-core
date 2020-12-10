package com.kkaminsky.medbotcore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MedbotCoreApplication

fun main(args: Array<String>) {
	runApplication<MedbotCoreApplication>(*args)
}
