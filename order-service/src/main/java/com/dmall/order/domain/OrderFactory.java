package com.dmall.order.domain;


import com.dmall.order.infrastructure.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
public class OrderFactory {

  @Autowired
  private OrderStateMachineFactory orderStateMachineFactory;

  @Autowired
  private OrderRepository repository;


  public Order build(Integer oid) {
    final Order order = repository.getOrderById(oid);

    final StateMachine<OrderStates, OrderEvents> stateMachine = orderStateMachineFactory.build(order);

    order.initialize(stateMachine, repository);

    return order;
  }
}
