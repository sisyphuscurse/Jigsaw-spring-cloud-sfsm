package com.dmall.order.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityFactory {

  @Autowired
  private OrderStateMachineFactory orderStateMachineFactory;

  @Autowired
  private IOrderRepository repository;


  public OrderEntity build(Integer oid) {
    final Order order = repository.getOrderById(oid);

    final OrderEntity orderEntity = new OrderEntity(order, repository);

    final StateMachine<OrderStates, OrderEvents> stateMachine = orderStateMachineFactory.build(orderEntity);

    orderEntity.initialize(stateMachine);

    return orderEntity;
  }
}
