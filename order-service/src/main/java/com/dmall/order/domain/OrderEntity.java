package com.dmall.order.domain;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.annotation.EventHeaders;
import org.springframework.statemachine.annotation.OnStateChanged;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.dmall.order.application.OrderService.ORDER_STATE_MACHINE;

//@Slf4j
@WithStateMachine(id = "orderStateMachine")
@Component("orderEntity")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderEntity {

  private ApplicationContext context;


  private final Order order;
  private StateMachine<OrderStates, OrderEvents> stateMachine;

  public OrderEntity() {
    this.order = new Order();
    this.order.setState(OrderStates.Created);
    restoreStateMachine();

  }

  public OrderEntity(ApplicationContext context, Order order) {
    this.context = context;
    this.order = order;
    this.stateMachine = getStateMachineFactory().getStateMachine(ORDER_STATE_MACHINE);
    restoreStateMachine();
  }

  private void restoreStateMachine() {
    StateMachineAccess<OrderStates, OrderEvents> region = stateMachine.getStateMachineAccessor().withRegion();
    region.resetStateMachine(
        new DefaultStateMachineContext<>(order.getState(), null, null, null, null, ORDER_STATE_MACHINE));
    this.stateMachine.start();
  }

  public void notifyPaid(String payment_id, String payment_time) {

    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderPaid)
        .setHeader("payment_id", payment_id)
        .setHeader("payment_time", payment_time)
        .build();

    stateMachine.sendEvent(message);
  }


  public void notifyInDelivery(String shipping_id, String shipping_time) {
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderShipped)
        .setHeader("shipping_id", shipping_id)
        .setHeader("shipping_time", shipping_time)
        .build();

    stateMachine.sendEvent(message);
  }

  public void notifyReceived(Integer shipping_id, String received_time) {
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderReceived)
        .setHeader("order", this)
        .setHeader("shipping_id", shipping_id)
        .setHeader("received_time", received_time)
        .build();

    stateMachine.sendEvent(message);
  }

  public void confirm() {

    stateMachine.sendEvent(OrderEvents.OrderConfirmed);
  }

  @OnTransition(source = "Created", target = "Paid")
  public void notifyPaid(@EventHeaders Map<String, Object> headers) {

    String payment_id = (String) headers.get("payment_id");
    String payment_time = (String) headers.get("payment_time");
    order.setPayment(new Payment(null, payment_id, order.getOid(), payment_time));
  }

  @OnTransition(source = "Paid", target = "InDelivery")
  public void updateToInDelivery(@EventHeaders Map<String, Object> headers) {

    String shipping_id = (String) headers.get("shipping_id");
    String shipping_time = (String) headers.get("shipping_time");

    order.setShipment(new Shipment(null, shipping_id, order.getOid(), shipping_time, null));
  }

  @OnTransition(source = "InDelivery", target = "Received")
  public void updateToReceived(@EventHeaders Map<String, Object> headers) {
    Integer shippingId = (Integer) headers.get("shipping_id");
    String receivedTime = (String) headers.get("received_time");
    order.getShipment().setReceived_time(receivedTime);
  }

  @OnTransition(target = "Confirmed")
  public void orderConfirm(@EventHeaders Map<String, Object> headers) {

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    order.setConfirm_time(format.format(new Date()));
  }

  @OnTransition(target = "Cancelled")
  public void cancelOrder() {
  }

  //Whether it will be invoked while state stays.
  @OnStateChanged
  public void save(StateMachine<OrderStates, OrderEvents> stateMachine) {
    order.setState(stateMachine.getState().getId());
    getOrderRepository().save(order);
  }

  public OrderDAO getOrderRepository() {
    return context.getBean(OrderDAO.class);
  }

  public StateMachineFactory<OrderStates, OrderEvents> getStateMachineFactory() {
    return context.getBean(StateMachineFactory.class);
  }

}
