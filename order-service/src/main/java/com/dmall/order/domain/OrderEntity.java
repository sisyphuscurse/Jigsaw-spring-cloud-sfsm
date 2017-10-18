
package com.dmall.order.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;

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

  public OrderEntity(Order order, IOrderRepository repository) {
    this.order = order;
    this.repository = repository;
  }

  // TODO: 18/10/2017 [BOILDER PLATE] This method should be refectory
  public void initialize(StateMachine<OrderStates, OrderEvents> stateMachine) {
    this.stateMachine = stateMachine;
    this.stateMachine.start();
  }


  public String getOrderStateMachineId() {
    return ORDER_STATE_MACHINE + order.getOid();
  }

  public void sendEvent(Message<OrderEvents> message) {
    exceptionHandler(stateMachine.sendEvent(message));
  }

  public void sendEvent(OrderEvents events) {
    exceptionHandler(stateMachine.sendEvent(events));
  }

  private void exceptionHandler(boolean succeed) {
    if (!succeed) {
      throw new RuntimeException("The event cannot be matched.");
    } else if (stateMachine.hasStateMachineError()) {
      RuntimeException exception = (RuntimeException) stateMachine.getExtendedState().getVariables().get("ERROR");

      if(exception instanceof OrderDomainException) {
        // TODO: 18/10/2017 recognize or handler exception
      }

      throw exception;
    }
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

  void onError(Exception exception) {
    // TODO: 18/10/2017 The exception can't be fetch in orderEntity, Using ExtendedState save exception.
    stateMachine.getExtendedState().getVariables().put("ERROR", exception);
    stateMachine.setStateMachineError(exception);
  }

  public OrderStates getOrderState() {
    return order.getState();
  }
}
