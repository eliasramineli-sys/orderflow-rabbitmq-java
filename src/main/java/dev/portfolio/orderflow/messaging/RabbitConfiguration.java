package dev.portfolio.orderflow.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    public static final String EXCHANGE = "orderflow.orders";
    public static final String QUEUE = "orderflow.orders.created";
    public static final String DLX = "orderflow.orders.dlx";
    public static final String DLQ = "orderflow.orders.dead";
    public static final String ROUTING_KEY = "orders.created";

    @Bean public DirectExchange orderExchange() { return new DirectExchange(EXCHANGE, true, false); }
    @Bean public DirectExchange deadLetterExchange() { return new DirectExchange(DLX, true, false); }
    @Bean public Queue orderQueue() {
        return QueueBuilder.durable(QUEUE).deadLetterExchange(DLX).deadLetterRoutingKey("orders.dead").build();
    }
    @Bean public Queue deadLetterQueue() { return QueueBuilder.durable(DLQ).build(); }
    @Bean public Binding orderBinding() { return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ROUTING_KEY); }
    @Bean public Binding deadLetterBinding() { return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("orders.dead"); }
    @Bean public Jackson2JsonMessageConverter jsonConverter() { return new Jackson2JsonMessageConverter(); }
}
