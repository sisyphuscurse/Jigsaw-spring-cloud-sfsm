package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

  private OrderJpaRepository repository;

  private OrderItemJpaRepository orderItemJpaRepository;

  private PaymentJpaRepository paymentJpaRepository;

  private ShipmentJpaRepository shipmentJpaRepository;

  @Autowired
  public OrderRepositoryImpl(OrderJpaRepository repository, OrderItemJpaRepository orderItemJpaRepository, PaymentJpaRepository paymentJpaRepository, ShipmentJpaRepository shipmentJpaRepository) {
    this.repository = repository;
    this.orderItemJpaRepository = orderItemJpaRepository;
    this.paymentJpaRepository = paymentJpaRepository;
    this.shipmentJpaRepository = shipmentJpaRepository;
  }

  @Override
  public Order getOrderById(Integer oid) {
    Order order = repository.findOne(oid);

    if (Objects.nonNull(order)) {
      order.setPayment(paymentJpaRepository.findByOid(oid));
      order.setShipment(shipmentJpaRepository.findByOid(oid));
    }

    return order;
  }

  @Override
  public Order save(Order order) {

    Order savedOrder = repository.save(order);

    order.getItems().stream()
        .forEach(c -> c.setOid(savedOrder.getOid()));

    orderItemJpaRepository.save(order.getItems());

    return savedOrder;
  }

  @Override
  public void notifyPaid(Order order) {
    repository.save(order);
    paymentJpaRepository.save(order.getPayment());
  }

  @Override
  public void notifyShipped(Order order) {
    repository.save(order); //maybe is not necessary.
    shipmentJpaRepository.save(order.getShipment());
  }

  @Override
  public void notifyReceived(Order order) {
    repository.save(order); //maybe is not necessary.
    shipmentJpaRepository.save(order.getShipment());
  }
}
