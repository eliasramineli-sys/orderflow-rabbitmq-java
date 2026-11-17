package dev.portfolio.orderflow.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id private UUID id;
    @Column(name = "order_id", nullable = false) private Long orderId;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "published_at") private Instant publishedAt;
    protected OutboxEvent() {}
    public OutboxEvent(Long orderId) {
        if (orderId == null) throw new IllegalArgumentException("Pedido obrigatório");
        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.createdAt = Instant.now();
    }
    public void markPublished(Instant now) { this.publishedAt = now; }
    public UUID getId() { return id; }
    public Long getOrderId() { return orderId; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getPublishedAt() { return publishedAt; }
}
