package dev.portfolio.orderflow.service;

import dev.portfolio.orderflow.api.OrderDtos.*;
import dev.portfolio.orderflow.domain.*;
import dev.portfolio.orderflow.repository.*;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orders;
    private final OutboxRepository outbox;
    public OrderService(OrderRepository orders, OutboxRepository outbox) {
        this.orders = orders;
        this.outbox = outbox;
    }
    @Transactional
    public OrderView create(CreateOrder request) {
        CustomerOrder order = orders.saveAndFlush(new CustomerOrder(request.customerName(), request.itemName(), request.quantity()));
        outbox.save(new OutboxEvent(order.getId()));
        return OrderView.from(order);
    }
    @Transactional(readOnly = true)
    public OrderView find(Long id) {
        return OrderView.from(orders.findById(id).orElseThrow(() -> new NoSuchElementException("Pedido não encontrado")));
    }
    @Transactional(readOnly = true)
    public List<OrderView> list() { return orders.findAll().stream().map(OrderView::from).toList(); }
}
