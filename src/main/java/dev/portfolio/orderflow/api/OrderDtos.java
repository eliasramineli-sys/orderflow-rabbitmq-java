package dev.portfolio.orderflow.api;

import dev.portfolio.orderflow.domain.CustomerOrder;
import dev.portfolio.orderflow.domain.OrderStatus;
import jakarta.validation.constraints.*;
import java.time.Instant;

public final class OrderDtos {
    private OrderDtos() {}
    public record CreateOrder(@NotBlank @Size(max = 120) String customerName,
                              @NotBlank @Size(max = 150) String itemName,
                              @Min(1) int quantity) {}
    public record OrderView(Long id, String customerName, String itemName, int quantity,
                            OrderStatus status, Instant createdAt, Instant completedAt) {
        public static OrderView from(CustomerOrder order) {
            return new OrderView(order.getId(), order.getCustomerName(), order.getItemName(),
                    order.getQuantity(), order.getStatus(), order.getCreatedAt(), order.getCompletedAt());
        }
    }
}
