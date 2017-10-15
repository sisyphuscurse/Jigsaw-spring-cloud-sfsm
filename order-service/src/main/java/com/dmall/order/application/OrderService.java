package com.dmall.order.application;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.domain.OrderRepository;
import com.dmall.order.domain.OrderStateMachineFactory;
import com.dmall.order.domain.OrderStates;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

  private final OrderStateMachineFactory factory;
  private OrderRepository repository;


  @Autowired
  public OrderService(OrderRepository repository,OrderStateMachineFactory factory) {
    this.repository = repository;
    this.factory = factory;
  }


  public void createOrder(Order order) {

    // due to order id is used as machine id, we should get order id before state machine created.

    order.setOrderCreation();
    Integer oid = repository.save(order);

    StateMachine<OrderStates, OrderEvents> stateMachine = factory.createAndStart(oid);

    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderCreated)
        .setHeader("order", order)
        .build();

    stateMachine.sendEvent(message);
  }

  public Order getOrderById(Integer oid) {
    return repository.getOrderById();
  }

  public void setPaid(Integer oid, String payment_id, String payment_time) {

    StateMachine<OrderStates, OrderEvents> stateMachine = factory.get(oid);

    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderPaid)
        .setHeader("oid", oid)
        .setHeader("payment_id", payment_id)
        .setHeader("payment_time", payment_time)
        .build();

    stateMachine.sendEvent(message);

  }
}
