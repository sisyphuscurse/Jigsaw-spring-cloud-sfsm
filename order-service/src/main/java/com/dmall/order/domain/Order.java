
package com.dmall.order.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer oid;

  @Column(nullable = false)
  private Integer uid;

  @Column(nullable = false)
  private BigDecimal total_price;

  @Column(nullable = false)
  private String create_time;

  @Enumerated(EnumType.STRING)
  private OrderStates state;

  @Transient
  private List<OrderItem> items;

  @Transient
  private StateMachine<OrderStates, OrderEvents> stateMachine;

  @Transient
  private Payment payment;

  @Transient
  private Shipment shipment;

  public void cancelOrder() {
    state = OrderStates.Cancelled;
  }


  public void setInDelivery() {
    state = OrderStates.InDelivery;
  }

  public void receive() {
    state = OrderStates.Received;
  }

  public void confirm() {
    state = OrderStates.Confirmed;
  }

  public void setOrderCreation() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String current_datetime = format.format(new Date());

    this.setState(OrderStates.Created);
    this.setCreate_time(current_datetime);
  }

  public void notifyOrderCreated() {
    sendEvent(OrderEvents.OrderCreated);
  }

  public void notifyPaid(String payment_id, String payment_time) {

    this.state = OrderStates.Paid;
    this.payment = new Payment(null, payment_id, oid, payment_time);

    sendEvent(OrderEvents.OrderPaid);
  }


  public void notifyInDelivery(String shipping_id, String shipping_time) {
    this.state = OrderStates.InDelivery;
    this.shipment = new Shipment(null, shipping_id, oid, shipping_time, null);

    sendEvent(OrderEvents.OrderShipped);
  }


  public void installStateMachine(StateMachine<OrderStates, OrderEvents> stateMachine) {
    this.stateMachine = stateMachine;
    stateMachine.start();
  }

  private void sendEvent(OrderEvents orderEvents) {
    Message<OrderEvents> message = MessageBuilder
        .withPayload(orderEvents)
        .setHeader("order", this)
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

    boolean flag = stateMachine.sendEvent(message);
  }
}
