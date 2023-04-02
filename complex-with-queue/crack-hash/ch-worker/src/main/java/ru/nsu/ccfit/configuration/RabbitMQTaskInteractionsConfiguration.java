package ru.nsu.ccfit.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTaskInteractionsConfiguration {

    @Value("${app.communication.rabbitmq-config.crack-hash-process.task-queue}")
    private String taskGettingQueue;
    @Value("${app.communication.rabbitmq-config.crack-hash-process.task-exchange}")
    private String taskGettingExchange;
    @Value("${app.communication.rabbitmq-config.crack-hash-process.task-routing}")
    private String taskGettingRouting;

    @Bean
    public Queue taskGettingQueue() {
        return new Queue(taskGettingQueue, true);
    }

    @Bean
    public DirectExchange taskGettingExchange() {
        return new DirectExchange(taskGettingExchange);
    }

    @Bean
    public Binding binding(Queue taskGettingQueue, DirectExchange taskGettingExchange) {
        return BindingBuilder
                .bind(taskGettingQueue)
                .to(taskGettingExchange)
                .with(taskGettingRouting);
    }

}

