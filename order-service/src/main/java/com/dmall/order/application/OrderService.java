package com.dmall.order.application;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderEntity;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.domain.OrderStateMachineFactory;
import com.dmall.order.infrastructure.repository.OrderItemJpaRepository;
import com.dmall.order.infrastructure.repository.OrderJpaRepository;
import com.dmall.order.infrastructure.repository.PaymentJpaRepository;
import com.dmall.order.infrastructure.repository.ShipmentJpaRepository;
import com.dmall.order.interfaces.assembler.OrderAssembler;
import com.dmall.order.interfaces.dto.CreateOrderRequest;
import com.dmall.order.interfaces.dto.CreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class OrderService {

  @Autowired
  private OrderAssembler orderAssembler;

  @Autowired
  private ApplicationContext context;

  @Autowired
  private OrderJpaRepository repository;

  @Autowired
  private OrderItemJpaRepository orderItemJpaRepository;

  @Autowired
  private PaymentJpaRepository paymentJpaRepository;

  @Autowired
  private ShipmentJpaRepository shipmentJpaRepository;

  @Autowired
  private OrderStateMachineFactory orderStateMachineFactory;

  public CreateOrderResponse createOrder(CreateOrderRequest orderRequest) {
    final Order order = save(orderAssembler.toDomainObject(orderRequest));
    return orderAssembler.toDTO(order);
  }

  public void notifyPaid(Integer oid, String payment_id, String payment_time) {

    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderPaid)
        .setHeader("payment_id", payment_id)
        .setHeader("payment_time", payment_time)
        .build();

    final OrderEntity orderEntity = buildOrderEntity(oid);
    orderEntity.sendEvent(message);
  }


  public void notifyInDelivery(Integer oid, String shipping_id, String shipping_time) {
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderShipped)
        .setHeader("shipping_id", shipping_id)
        .setHeader("shipping_time", shipping_time)
        .build();

    final OrderEntity orderEntity = buildOrderEntity(oid);
    orderEntity.sendEvent(message);
  }

  public void notifyReceived(Integer oid, Integer shipping_id, String received_time) {
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderReceived)
        .setHeader("shipping_id", shipping_id)
        .setHeader("received_time", received_time)
        .build();

    final OrderEntity orderEntity = buildOrderEntity(oid);
    orderEntity.sendEvent(message);
  }

  public void confirmOrder(Integer oid, String uid) {

    final OrderEntity orderEntity = buildOrderEntity(oid);
    orderEntity.sendEvent(OrderEvents.OrderConfirmed);
  }

  private OrderEntity buildOrderEntity(Integer oid) {
    final Order order = getOrderById(oid);
    return context.getBean(OrderEntity.class, order, orderStateMachineFactory, this);
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
