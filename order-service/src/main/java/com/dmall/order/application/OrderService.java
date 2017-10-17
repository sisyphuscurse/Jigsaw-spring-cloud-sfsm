package com.dmall.order.application;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderFactory;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.infrastructure.repository.OrderRepository;
import com.dmall.order.interfaces.assembler.OrderAssembler;
import com.dmall.order.interfaces.dto.CreateOrderRequest;
import com.dmall.order.interfaces.dto.CreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

  @Autowired
  private OrderAssembler orderAssembler;

  @Autowired
  private OrderRepository repository;

  @Autowired
  private OrderFactory orderFactory;

  public CreateOrderResponse createOrder(CreateOrderRequest orderRequest) {
    final Order order = repository.save(orderAssembler.toDomainObject(orderRequest));
    return orderAssembler.toDTO(order);
  }

  public void notifyPaid(Integer oid, String payment_id, String payment_time) {

    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderPaid)
        .setHeader("payment_id", payment_id)
        .setHeader("payment_time", payment_time)
        .build();

    final Order order = orderFactory.build(oid);
    order.sendEvent(message);
  }

  public void notifyInDelivery(Integer oid, String shipping_id, String shipping_time) {
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderShipped)
        .setHeader("shipping_id", shipping_id)
        .setHeader("shipping_time", shipping_time)
        .build();

    final Order order = orderFactory.build(oid);
    order.sendEvent(message);
  }

  public void notifyReceived(Integer oid, Integer shipping_id, String received_time) {
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderReceived)
        .setHeader("shipping_id", shipping_id)
        .setHeader("received_time", received_time)
        .build();

    final Order order = orderFactory.build(oid);
    order.sendEvent(message);
  }

  public void confirmOrder(Integer oid, String uid) {

    final Order order = orderFactory.build(oid);
    order.sendEvent(OrderEvents.OrderConfirmed);
  }


  public Order getOrderById(Integer oid) {
    return repository.getOrderById(oid);
  }

}
