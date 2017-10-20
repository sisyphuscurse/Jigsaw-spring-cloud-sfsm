package com.dmall.order.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
//TODO [Barry][SSM] 这也是因为Spring FSM带来的Boiler Plate，当中没有任何领域逻辑，没有任何状态机本身的信息
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
