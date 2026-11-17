package dev.portfolio.orderflow.service;

import dev.portfolio.orderflow.messaging.OrderCreatedEvent;
import dev.portfolio.orderflow.messaging.RabbitConfiguration;
import dev.portfolio.orderflow.repository.OrderRepository;
import java.time.Instant;
import java.util.NoSuchElementException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderEventConsumer {
    private final OrderRepository orders;
    public OrderEventConsumer(OrderRepository orders) { this.orders = orders; }
    @RabbitListener(queues = RabbitConfiguration.QUEUE)
    @Transactional
    public void handle(OrderCreatedEvent event) {
        var order = orders.findById(event.orderId())
                .orElseThrow(() -> new NoSuchElementException("Pedido do evento não encontrado"));
        // A mesma mensagem pode ser entregue mais de uma vez.
        order.complete(Instant.now());
        orders.saveAndFlush(order);
    }
}
