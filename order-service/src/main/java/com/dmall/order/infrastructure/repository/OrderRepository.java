package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderEntity;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.domain.OrderStateMachineFactory;
import com.dmall.order.interfaces.assembler.OrderAssembler;
import com.dmall.order.interfaces.dto.CreateOrderRequest;
import com.dmall.order.interfaces.dto.CreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
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
