package com.dmall.order.domain;


import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class OrderStateMachineFactory {

  public StateMachine<OrderStates, OrderEvents> build(OrderEntity orderEntity) {
    StateMachineBuilder.Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();

    try {
      builder.configureConfiguration()
          .withConfiguration()
          .machineId(orderEntity.getOrderStateMachineId())
          .listener(orderEntity.listener())
          .beanFactory(new StaticListableBeanFactory());

      builder.configureStates()
          .withStates()
          .initial(OrderStates.Created)
          .states(EnumSet.allOf(OrderStates.class));

      builder.configureTransitions()
          .withExternal()
          .source(OrderStates.Created).target(OrderStates.Paid)
          .event(OrderEvents.OrderPaid)
          .action(orderEntity.notifyPaidAction())
          .and()
          .withExternal()
          .source(OrderStates.Paid).target(OrderStates.InDelivery)
          .event(OrderEvents.OrderShipped)
          .action(orderEntity.notifyShippedAction())
          .and()
          .withExternal()
          .source(OrderStates.InDelivery).target(OrderStates.Received)
          .event(OrderEvents.OrderReceived)
          .action(orderEntity.notifyReceivedAction())
          .and()
          .withExternal()
          .source(OrderStates.Received).target(OrderStates.Confirmed)
          .event(OrderEvents.OrderConfirmed)
          .action(orderEntity.confirmOrder())
          .and()
          .withExternal()
          .source(OrderStates.Created).target(OrderStates.Cancelled)
          .event(OrderEvents.OrderCancelled)
          .action(orderEntity.cancelOrder());
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return builder.build();
  }


}
