package ru.nsu.ccfit.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.MarshallingMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class RabbitMQCommonConfiguration {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(messageConverter);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public Jaxb2Marshaller jaxbMarshaller() {
        var jaxbMarshaller = new Jaxb2Marshaller();
        jaxbMarshaller.setPackagesToScan("ru.nsu.ccfit.schema");
        return jaxbMarshaller;
    }

    @Bean
    public MarshallingMessageConverter marshallingMessageConverter(Jaxb2Marshaller jaxbMarshaller) {
        MarshallingMessageConverter messageConverter = new MarshallingMessageConverter();
        messageConverter.setMarshaller(jaxbMarshaller);
        messageConverter.setUnmarshaller(jaxbMarshaller);
        messageConverter.setContentType(MessageProperties.CONTENT_TYPE_XML);
        return messageConverter;
    }

}

