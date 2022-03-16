package com.example.messagingredis

import mu.*
import java.util.concurrent.atomic.*

@Suppress("unused")
class Receiver {

    companion object{
        private val logger = KotlinLogging.logger {}
    }

    private val counter = AtomicInteger()

    fun receiveMessage(message: String){
        logger.info { "Received<$message>" }
        counter.incrementAndGet()
    }

    fun getCount(): Int{
        return counter.get()
    }
}