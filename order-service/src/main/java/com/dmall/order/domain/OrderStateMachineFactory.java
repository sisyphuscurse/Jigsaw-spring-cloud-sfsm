package com.dmall.order.domain;


import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class OrderStateMachineFactory {

  public StateMachine<OrderStates, OrderEvents> buildAndStart(OrderEntity orderEntity) {
    StateMachineBuilder.Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();

    try {
      builder.configureConfiguration()
          .withConfiguration()
          .machineId(orderEntity.getOrderStateMachineId())
          .listener(listener(orderEntity))
          .beanFactory(new StaticListableBeanFactory());

      builder.configureStates()
          .withStates()
          .initial(OrderStates.Created)
          .states(EnumSet.allOf(OrderStates.class));

      builder.configureTransitions()
          .withExternal()
          .source(OrderStates.Created).target(OrderStates.Paid)
          .event(OrderEvents.OrderPaid)
          .action(notifyPaidAction(orderEntity))
          .and()
          .withExternal()
          .source(OrderStates.Paid).target(OrderStates.InDelivery)
          .event(OrderEvents.OrderShipped)
          .action(notifyShippedAction(orderEntity))
          .and()
          .withExternal()
          .source(OrderStates.InDelivery).target(OrderStates.Received)
          .event(OrderEvents.OrderReceived)
          .action(notifyReceivedAction(orderEntity))
          .and()
          .withExternal()
          .source(OrderStates.Received).target(OrderStates.Confirmed)
          .event(OrderEvents.OrderConfirmed)
          .action(confirmOrder(orderEntity))
          .and()
          .withExternal()
          .source(OrderStates.Created).target(OrderStates.Cancelled)
          .event(OrderEvents.OrderCancelled)
          .action(cancelOrder());
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    final StateMachine<OrderStates, OrderEvents> stateMachine = builder.build();

    restoreStateMachine(stateMachine, orderEntity);

    stateMachine.start();

    return stateMachine;
  }

  private void restoreStateMachine(StateMachine<OrderStates, OrderEvents> stateMachine, OrderEntity entity) {
    StateMachineAccess<OrderStates, OrderEvents> region = stateMachine.getStateMachineAccessor().withRegion();
    final OrderStates state = entity.getOrderState();
    final String machinedId = entity.getOrderStateMachineId();
    region.resetStateMachine(
        new DefaultStateMachineContext<>(state, null, null, null, null, machinedId));
  }

  Action<OrderStates, OrderEvents> notifyPaidAction(OrderEntity entity) {
    return context -> {
      final MessageHeaders headers = context.getMessageHeaders();
      String payment_id = (String) headers.get("payment_id");
      String payment_time = (String) headers.get("payment_time");
      entity.onPaid(payment_id, payment_time);
    };
  }

  Action<OrderStates, OrderEvents> notifyShippedAction(OrderEntity entity) {

    return context -> {
      final MessageHeaders headers = context.getMessageHeaders();
      String shipping_id = (String) headers.get("shipping_id");
      String shipping_time = (String) headers.get("shipping_time");

      entity.onShipped(shipping_id, shipping_time);
    };
  }

  Action<OrderStates, OrderEvents> notifyReceivedAction(OrderEntity entity) {

    return context -> {
      final MessageHeaders headers = context.getMessageHeaders();
      String receivedTime = (String) headers.get("received_time");
      entity.onReceived(receivedTime);
    };
  }

  Action<OrderStates, OrderEvents> confirmOrder(OrderEntity entity) {
    return context -> {
      entity.onConfirmed();
    };
  }


  Action<OrderStates, OrderEvents> cancelOrder() {
    return context -> {

    };
  }

  StateMachineListenerAdapter<OrderStates, OrderEvents> listener(OrderEntity entity) {
    return new StateMachineListenerAdapter<OrderStates, OrderEvents>() {
      @Override
      public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
        entity.onStateChanged();
        super.stateChanged(from, to);
      }
    };
  }

}
