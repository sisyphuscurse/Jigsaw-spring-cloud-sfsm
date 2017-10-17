package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Repository
@Transactional
public class OrderRepository {


  @Autowired
  private OrderJpaRepository repository;

  @Autowired
  private OrderItemJpaRepository orderItemJpaRepository;

  @Autowired
  private PaymentJpaRepository paymentJpaRepository;

  @Autowired
  private ShipmentJpaRepository shipmentJpaRepository;

  public Order getOrderById(Integer oid) {
    Order order = repository.findOne(oid);

    if (Objects.nonNull(order)) {
      order.setPayment(paymentJpaRepository.findByOid(oid));
      order.setShipment(shipmentJpaRepository.findByOid(oid));
      order.setItems(orderItemJpaRepository.findByOid(oid));
    }

    return order;
  }

  public Order save(Order order) {

    Order savedOrder = repository.save(order);

    if (Objects.nonNull(order.getItems())) {
      order.getItems().stream()
          .forEach(c -> c.setOid(savedOrder.getOid()));

      orderItemJpaRepository.save(order.getItems());
    }

    if (Objects.nonNull(order.getPayment())) {
      paymentJpaRepository.save(order.getPayment());
    }

    if (Objects.nonNull(order.getShipment())) {
      shipmentJpaRepository.save(order.getShipment());
    }

    return savedOrder;
  }
}
