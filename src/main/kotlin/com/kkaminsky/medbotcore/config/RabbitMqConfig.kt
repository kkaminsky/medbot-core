package com.kkaminsky.medbotcore.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.rabbitmq.client.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.*
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*


@EnableRabbit
@Configuration
class RabbitMqConfig constructor(
        private val connectionFactory: org.springframework.amqp.rabbit.connection.ConnectionFactory,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun jsonMessageConverter(): MessageConverter {
        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())
        mapper.registerModule(JavaTimeModule())
        return Jackson2JsonMessageConverter(mapper)
    }

    @Bean
    fun accountSync(): Queue {
        return Queue("medbot-account-sync")
    }

    @Bean
    fun report(): Queue {
        return Queue("medbot-report")
    }

    @Bean
    fun fanoutExchangeA(): FanoutExchange {
        return FanoutExchange("medbot-exchange-account-sync")
    }

    @Bean
    fun rabbitTemplate(): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter=jsonMessageConverter()
        return template
    }

    @Bean
    fun binding1(): Binding {
        return BindingBuilder.bind(accountSync()).to(fanoutExchangeA())
    }
}