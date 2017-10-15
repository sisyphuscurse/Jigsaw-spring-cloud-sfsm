package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

  private OrderJpaRepository repository;

  private OrderItemJpaRepository orderItemJpaRepository;

  @Autowired
  public OrderRepositoryImpl(OrderJpaRepository repository, OrderItemJpaRepository orderItemJpaRepository) {
    this.repository = repository;
    this.orderItemJpaRepository = orderItemJpaRepository;
  }

  @Override
  public Order getOrderById() {
    return null;
  }

  @Override
  public Integer save(Order order) {

    Order savedOrder = repository.save(order);

    order.getItems().stream()
        .forEach(c -> c.setOid(savedOrder.getOid()));

    orderItemJpaRepository.save(order.getItems());

    return order.getOid();
  }
}
