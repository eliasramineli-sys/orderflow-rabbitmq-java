package dev.portfolio.orderflow.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_name", nullable = false, length = 120)
    private String customerName;
    @Column(name = "item_name", nullable = false, length = 150)
    private String itemName;
    @Column(nullable = false)
    private int quantity;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.CREATED;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "completed_at")
    private Instant completedAt;
    @Version private long version;

    protected CustomerOrder() {}
    public CustomerOrder(String customerName, String itemName, int quantity) {
        if (customerName == null || customerName.isBlank() || itemName == null || itemName.isBlank() || quantity < 1)
            throw new IllegalArgumentException("Pedido inválido");
        this.customerName = customerName.strip();
        this.itemName = itemName.strip();
        this.quantity = quantity;
        this.createdAt = Instant.now();
    }
    /** Retorna false para entrega duplicada (idempotência). */
    public boolean complete(Instant now) {
        if (status == OrderStatus.COMPLETED) return false;
        if (now == null || now.isBefore(createdAt)) throw new IllegalArgumentException("Data inválida");
        status = OrderStatus.COMPLETED;
        completedAt = now;
        return true;
    }
    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getCompletedAt() { return completedAt; }
}
