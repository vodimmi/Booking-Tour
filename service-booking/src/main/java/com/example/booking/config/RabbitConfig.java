package com.example.booking.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "booking-events-exchange";
    public static final String QUEUE = "booking-events";
    public static final String ROUTING_KEY = "booking.events";
    public static final String PAYMENT_COMPLETED_QUEUE = "payment.completed.queue";

    @Bean
    public Queue bookingQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Queue paymentCompletedQueue() {
        return new Queue(PAYMENT_COMPLETED_QUEUE, true);
    }

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding bookingBinding(DirectExchange bookingExchange) {
        return BindingBuilder.bind(bookingQueue()).to(bookingExchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
