
package com.dmall.order.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderEntity {

  public static final String ORDER_STATE_MACHINE = "orderStateMachine";

  private Order order;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @Transient
  private StateMachine<OrderStates, OrderEvents> stateMachine;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @Transient
  private IOrderRepository repository;

  public OrderEntity(Order order) {
    this.order = order;
  }

  public void initialize(StateMachine<OrderStates, OrderEvents> stateMachine, IOrderRepository repository) {
    this.stateMachine = stateMachine;
    this.repository = repository;

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

  void onPaid(String payment_id, String payment_time) {
    order.setPayment(new Payment(null, payment_id, order.getOid(), payment_time));
  }

  void onShipped(String shipping_id, String shipping_time) {
    order.setShipment(new Shipment(null, shipping_id, order.getOid(), shipping_time, null));
  }

  void onReceived(String receivedTime) {
    order.getShipment().setReceived_time(receivedTime);
  }

  void onConfirmed() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    order.setConfirm_time(format.format(new Date()));
  }

  void onStateChanged() {
    order.setState(stateMachine.getState().getId());
    repository.save(order);
  }

  public OrderStates getOrderState() {
      return order.getState();
  }
}
