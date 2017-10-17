package com.dmall.order.domain;


import com.dmall.order.infrastructure.repository.OrderRepository;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderEntity {

  public static final String ORDER_STATE_MACHINE = "orderStateMachine";

  private final Order order;

  private StateMachine<OrderStates, OrderEvents> stateMachine;

  private OrderRepository repository;

  public OrderEntity(Order order, OrderStateMachineFactory factory, OrderRepository repository) {
    this.order = order;
    this.repository = repository;
    this.stateMachine = factory.buildAndStart(this);
  }

  public OrderStates getOrderState() {
    return order.getState();
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

  public void onPaid(String payment_id, String payment_time) {
    order.setPayment(new Payment(null, payment_id, order.getOid(), payment_time));
  }

  public void onShipped(String shipping_id, String shipping_time) {
    order.setShipment(new Shipment(null, shipping_id, order.getOid(), shipping_time, null));
  }

  public void onReceived(String receivedTime) {
    order.getShipment().setReceived_time(receivedTime);
  }

  public void onConfirmed() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    order.setConfirm_time(format.format(new Date()));
  }

  public void onStateChanged() {
    order.setState(stateMachine.getState().getId());
    repository.save(order);
  }

}
