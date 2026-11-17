package dev.portfolio.orderflow.service;

import dev.portfolio.orderflow.messaging.OrderCreatedEvent;
import dev.portfolio.orderflow.messaging.RabbitConfiguration;
import dev.portfolio.orderflow.repository.OutboxRepository;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {
    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private final OutboxRepository outbox;
    private final RabbitTemplate rabbit;
    public OutboxPublisher(OutboxRepository outbox, RabbitTemplate rabbit) {
        this.outbox = outbox; this.rabbit = rabbit;
    }
    /** Confirmação do broker antes de marcar o evento como publicado.
     * A publicação é at-least-once: falha entre o confirm e o commit pode duplicar a mensagem.
     */
    @Scheduled(fixedDelayString = "${orderflow.dispatch-delay-ms:3000}")
    @Transactional
    public void dispatch() {
        for (var event : outbox.findTop50ByPublishedAtIsNullOrderByCreatedAtAsc()) {
            try {
                rabbit.invoke(operations -> {
                    operations.convertAndSend(RabbitConfiguration.EXCHANGE, RabbitConfiguration.ROUTING_KEY,
                            new OrderCreatedEvent(event.getId(), event.getOrderId()));
                    operations.waitForConfirmsOrDie(5000);
                    return null;
                });
                event.markPublished(Instant.now());
                outbox.save(event);
            } catch (Exception ex) {
                log.warn("Falha ao publicar evento {}: {}", event.getId(), ex.toString());
                // Continua pendente no outbox para tentativa posterior.
            }
        }
    }
}
