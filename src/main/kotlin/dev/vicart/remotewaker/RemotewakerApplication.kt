package dev.vicart.remotewaker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RemotewakerApplication

fun main(args: Array<String>) {
    runApplication<RemotewakerApplication>(*args)
}
