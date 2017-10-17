package com.dmall.order.domain;


import com.dmall.order.application.OrderService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderEntity {

  public static final String ORDER_STATE_MACHINE = "orderStateMachine";

  private final Order order;

  private StateMachine<OrderStates, OrderEvents> stateMachine;

  private OrderStateMachineFactory factory;

  private OrderService orderService;

  public OrderEntity(Order order, OrderStateMachineFactory factory, OrderService orderService) {
    this.order = order;
    this.factory = factory;
    this.orderService = orderService;

    this.stateMachine = this.factory.build(this);

    restoreStateMachine();
  }

  private void restoreStateMachine() {
    StateMachineAccess<OrderStates, OrderEvents> region = stateMachine.getStateMachineAccessor().withRegion();
    region.resetStateMachine(
        new DefaultStateMachineContext<>(order.getState(), null, null, null, null, getOrderStateMachineId()));
    this.stateMachine.start();
  }

  public String getOrderStateMachineId() {
    return ORDER_STATE_MACHINE + order.getOid();
  }

  public void sendEvent(Message<OrderEvents> message) {

    stateMachine.sendEvent(message);
  }

  public void sendEvent(OrderEvents events) {

    stateMachine.sendEvent(events);
  }



  Action<OrderStates, OrderEvents> notifyPaidAction() {
    return context -> {
      final MessageHeaders headers = context.getMessageHeaders();
      String payment_id = (String) headers.get("payment_id");
      String payment_time = (String) headers.get("payment_time");
      order.setPayment(new Payment(null, payment_id, order.getOid(), payment_time));
    };
  }

  Action<OrderStates, OrderEvents> notifyShippedAction() {

    return context -> {
      final MessageHeaders headers = context.getMessageHeaders();
      String shipping_id = (String) headers.get("shipping_id");
      String shipping_time = (String) headers.get("shipping_time");

      order.setShipment(new Shipment(null, shipping_id, order.getOid(), shipping_time, null));
    };
  }

  Action<OrderStates, OrderEvents> notifyReceivedAction() {

    return context -> {
      final MessageHeaders headers = context.getMessageHeaders();
      String receivedTime = (String) headers.get("received_time");
      order.getShipment().setReceived_time(receivedTime);
    };
  }


  Action<OrderStates, OrderEvents> confirmOrder() {
    return context -> {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      order.setConfirm_time(format.format(new Date()));
    };
  }

  Action<OrderStates, OrderEvents> cancelOrder() {
    return context -> {

    };
  }

  StateMachineListenerAdapter<OrderStates, OrderEvents> listener() {
    return new StateMachineListenerAdapter<OrderStates, OrderEvents>()  {
      @Override
      public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
        order.setState(stateMachine.getState().getId());
        orderService.save(order);
        super.stateChanged(from, to);
      }
    };
  }
}
