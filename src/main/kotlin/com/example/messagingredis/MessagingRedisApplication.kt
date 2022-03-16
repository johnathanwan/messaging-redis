package com.example.messagingredis

import mu.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import kotlin.system.*

@SpringBootApplication
class MessagingRedisApplication{

    companion object {
        private val logger = KotlinLogging.logger {}
        @JvmStatic
        fun main(args: Array<String>) {
            val ctx = runApplication<MessagingRedisApplication>(*args)

            val template = ctx.getBean(StringRedisTemplate::class.java)
            val receiver = ctx.getBean(Receiver::class.java)

            while (receiver.getCount() == 0) {

                logger.info { "Sending message..." }
                template.convertAndSend("chat", "Hello from Redis!")
                Thread.sleep(500L)
            }
            exitProcess(0)
        }
    }

    @Bean
    fun container(connectionFactory: RedisConnectionFactory,
                listenerAdapter: MessageListenerAdapter): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(connectionFactory)
        container.addMessageListener(listenerAdapter, PatternTopic("chat"))
        return container
    }

    @Bean
    fun listenerAdapter(receiver: Receiver): MessageListenerAdapter {
        return MessageListenerAdapter(receiver,"receiveMessage")
    }

    @Bean
    fun receiver(): Receiver {
        return Receiver()
    }

    @Bean
    fun template(connectionFactory: RedisConnectionFactory): StringRedisTemplate {
        return StringRedisTemplate(connectionFactory)
    }

}


