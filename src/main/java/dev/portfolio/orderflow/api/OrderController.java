package dev.portfolio.orderflow.api;

import dev.portfolio.orderflow.api.OrderDtos.*;
import dev.portfolio.orderflow.service.OrderService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }
    @PostMapping
    public ResponseEntity<OrderView> create(@Valid @RequestBody CreateOrder request) {
        OrderView order = service.create(request);
        return ResponseEntity.accepted().location(URI.create("/api/v1/orders/" + order.id())).body(order);
    }
    @GetMapping("/{id}") public OrderView find(@PathVariable Long id) { return service.find(id); }
    @GetMapping public List<OrderView> list() { return service.list(); }
}
