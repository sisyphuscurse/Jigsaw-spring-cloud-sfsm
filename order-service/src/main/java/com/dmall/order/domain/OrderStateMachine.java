package com.dmall.order.domain;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.EventHeaders;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

@Slf4j
@WithStateMachine(id = "orderStateMachine")
public class OrderStateMachine {

  @Autowired
  private WebApplicationContext context;

  @OnTransition(source = "Idle", target = "Created")
  public void createOrder(@EventHeaders Map<String, Object> headers) {
    OrderRepository repository = context.getBean(OrderRepository.class);
    Order order = (Order) headers.get("order");
    order.setOrderCreation();
    repository.save(order);
  }

  @OnTransition(source = "Created", target = "Paid")
  public void notifyPaid(@EventHeaders Map<String, Object> headers) {
    OrderRepository repository = context.getBean(OrderRepository.class);
    Order order = (Order) headers.get("order");
    repository.notifyPaid(order);

  }

  @OnTransition(source = "Paid", target = "InDelivery")
  public void updateToInDelivery(@EventHeaders Map<String, Object> headers) {
    OrderRepository repository = context.getBean(OrderRepository.class);
    Order order = (Order) headers.get("order");
    repository.notifyShipped(order);
  }

  @OnTransition(source = "InDelivery", target = "Received")
  public void updateToReceived(@EventHeaders Map<String, Object> headers) {
    Order order = (Order) headers.get("order");
    Integer shippingId = (Integer) headers.get("shipping_id");
    String receivedTime = (String) headers.get("received_time");
    order.getShipment().setReceived_time(receivedTime);
    order.setState(OrderStates.Received);
    OrderRepository repository = context.getBean(OrderRepository.class);

    repository.notifyReceived(order);
  }

  @OnTransition(target = "Confirmed")
  public void confirm() {

  }

  @OnTransition(target = "Cancelled")
  public void cancelOrder() {
  }

}
