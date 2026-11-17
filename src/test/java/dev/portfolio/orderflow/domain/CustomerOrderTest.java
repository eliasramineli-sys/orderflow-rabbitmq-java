package dev.portfolio.orderflow.domain;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerOrderTest {
    @Test void createdOrderCanBeCompletedOnce() {
        var order = new CustomerOrder("Maria", "Livro", 2);
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertTrue(order.complete(Instant.now()));
        assertFalse(order.complete(Instant.now()));
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        assertNotNull(order.getCompletedAt());
    }
    @Test void rejectsInvalidQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new CustomerOrder("Maria", "Livro", 0));
    }
}
