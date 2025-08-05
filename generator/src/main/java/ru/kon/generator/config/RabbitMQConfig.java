package ru.kon.generator.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String REQUEST_EXCHANGE_NAME = "report-request-exchange";
    public static final String REQUEST_QUEUE_NAME = "report.request.queue";
    public static final String REQUEST_ROUTING_KEY = "report.request.key";

    public static final String RESULT_EXCHANGE_NAME = "report-result-exchange";
    public static final String RESULT_QUEUE_NAME = "report.result.queue";
    public static final String RESULT_ROUTING_KEY = "report.result.key";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
