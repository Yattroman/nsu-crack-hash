package ru.nsu.ccfit.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nsu.ccfit.port.out.rabbitmq.IWorkerCommunicationService;
import ru.nsu.ccfit.service.specific.ICrackHashInteractionService;
import ru.nsu.ccfit.service.specific.ICrackHashTaskConverter;
import ru.nsu.ccfit.task.MessageInQueueCheckTask;

@Configuration
public class RabbitMQTaskInteractionsConfiguration {

    @Value("${app.communication.rabbitmq-config.crack-hash-process.task-result-queue}")
    private String taskResultGettingQueue;
    @Value("${app.communication.rabbitmq-config.crack-hash-process.task-result-exchange}")
    private String taskResultGettingExchange;
    @Value("${app.communication.rabbitmq-config.crack-hash-process.task-result-routing}")
    private String taskResultGettingRouting;

    @Bean
    public Queue taskResultGettingQueue() {
        return new Queue(taskResultGettingQueue, true);
    }

    @Bean
    public DirectExchange taskResultGettingExchange() {
        return new DirectExchange(taskResultGettingExchange);
    }

    @Bean
    public Binding binding(Queue taskResultGettingQueue, DirectExchange taskResultGettingExchange) {
        return BindingBuilder
                .bind(taskResultGettingQueue)
                .to(taskResultGettingExchange)
                .with(taskResultGettingRouting);
    }

    @Bean
    public ApplicationRunner customizeConnectionFactory(ICrackHashInteractionService crackHashInteractionService,
                                                        IWorkerCommunicationService workerCommunicationService,
                                                        ICrackHashTaskConverter crackHashTaskConverter,
                                                        ConnectionFactory connectionFactory) {
        connectionFactory.addConnectionListener(connection ->
                new MessageInQueueCheckTask(
                        crackHashInteractionService,
                        workerCommunicationService,
                        crackHashTaskConverter
                ).run()
        );

        return args -> {
        };
    }

}

