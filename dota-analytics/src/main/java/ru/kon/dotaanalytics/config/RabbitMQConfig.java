package ru.kon.dotaanalytics.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange requestExchange() {
        return new DirectExchange(REQUEST_EXCHANGE_NAME);
    }

    @Bean
    public Binding requestBinding(Queue requestQueue, DirectExchange requestExchange) {
        return BindingBuilder.bind(requestQueue).to(requestExchange).with(REQUEST_ROUTING_KEY);
    }

    @Bean
    public Queue queue() {
        return new Queue(RESULT_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(RESULT_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RESULT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
