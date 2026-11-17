package dev.portfolio.orderflow.messaging;

import java.util.UUID;

public record OrderCreatedEvent(UUID eventId, Long orderId) {}
