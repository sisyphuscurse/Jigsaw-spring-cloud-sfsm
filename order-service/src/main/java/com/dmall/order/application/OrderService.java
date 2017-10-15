package com.dmall.order.application;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.domain.OrderRepository;
import com.dmall.order.domain.OrderStateMachineFactory;
import com.dmall.order.domain.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderStateMachineFactory factory;
  private OrderRepository repository;


  @Autowired
  public OrderService(OrderRepository repository, OrderStateMachineFactory factory) {
    this.repository = repository;
    this.factory = factory;
  }


  public void createOrder(Order order) {

    StateMachine<OrderStates, OrderEvents> stateMachine = factory.newStatemachine("orderStateMachine");

    order.installStateMachine(stateMachine);

    order.notifyOrderCreated();
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
