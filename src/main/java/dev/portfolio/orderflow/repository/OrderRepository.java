package dev.portfolio.orderflow.repository;

import dev.portfolio.orderflow.domain.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {}
