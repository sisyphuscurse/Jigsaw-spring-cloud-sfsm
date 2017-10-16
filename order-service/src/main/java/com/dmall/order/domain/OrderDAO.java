package com.dmall.order.domain;

import com.dmall.order.domain.Order;
import com.dmall.order.infrastructure.repository.OrderItemJpaRepository;
import com.dmall.order.infrastructure.repository.OrderJpaRepository;
import com.dmall.order.infrastructure.repository.PaymentJpaRepository;
import com.dmall.order.infrastructure.repository.ShipmentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class OrderDAO {

  private OrderJpaRepository repository;

  private OrderItemJpaRepository orderItemJpaRepository;

  private PaymentJpaRepository paymentJpaRepository;

  private ShipmentJpaRepository shipmentJpaRepository;

  @Autowired
  public OrderDAO(OrderJpaRepository repository, OrderItemJpaRepository orderItemJpaRepository, PaymentJpaRepository paymentJpaRepository, ShipmentJpaRepository shipmentJpaRepository) {
    this.repository = repository;
    this.orderItemJpaRepository = orderItemJpaRepository;
    this.paymentJpaRepository = paymentJpaRepository;
    this.shipmentJpaRepository = shipmentJpaRepository;
  }

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

    order.getItems().stream()
        .forEach(c -> c.setOid(savedOrder.getOid()));

    orderItemJpaRepository.save(order.getItems());

    return savedOrder;
  }

  public void notifyPaid(Order order) {
    repository.save(order);
    paymentJpaRepository.save(order.getPayment());
  }

  public void notifyShipped(Order order) {
    repository.save(order); //maybe is not necessary.
    shipmentJpaRepository.save(order.getShipment());
  }

  public void notifyReceived(Order order) {
    repository.save(order); //maybe is not necessary.
    shipmentJpaRepository.save(order.getShipment());
  }
}
