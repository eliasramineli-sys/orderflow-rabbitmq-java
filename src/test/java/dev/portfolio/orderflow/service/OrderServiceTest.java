package dev.portfolio.orderflow.service;

import dev.portfolio.orderflow.api.OrderDtos.CreateOrder;
import dev.portfolio.orderflow.domain.*;
import dev.portfolio.orderflow.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Test void createsOrderAndOutboxEventInSameUseCase() {
        var orders = mock(OrderRepository.class);
        var outbox = mock(OutboxRepository.class);
        when(orders.saveAndFlush(any(CustomerOrder.class))).thenAnswer(call -> {
            CustomerOrder order = call.getArgument(0);
            try {
                var field = CustomerOrder.class.getDeclaredField("id");
                field.setAccessible(true);
                field.set(order, 42L);
            } catch (ReflectiveOperationException ex) { throw new RuntimeException(ex); }
            return order;
        });
        var result = new OrderService(orders, outbox).create(new CreateOrder("Ana", "Mouse", 1));
        assertEquals(42L, result.id());
        assertEquals(OrderStatus.CREATED, result.status());
        var event = ArgumentCaptor.forClass(OutboxEvent.class);
        verify(outbox).save(event.capture());
        assertEquals(42L, event.getValue().getOrderId());
    }
}
