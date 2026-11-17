package dev.portfolio.orderflow.service;

import dev.portfolio.orderflow.domain.*;
import dev.portfolio.orderflow.messaging.OrderCreatedEvent;
import dev.portfolio.orderflow.repository.OrderRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderEventConsumerTest {
    @Test void duplicateDeliveryDoesNotReprocessCompletedOrder() {
        var orders = mock(OrderRepository.class);
        var order = new CustomerOrder("Ana", "Livro", 1);
        when(orders.findById(1L)).thenReturn(Optional.of(order));
        var consumer = new OrderEventConsumer(orders);
        var event = new OrderCreatedEvent(UUID.randomUUID(), 1L);
        consumer.handle(event);
        consumer.handle(event);
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        verify(orders, times(2)).saveAndFlush(order);
    }
}
