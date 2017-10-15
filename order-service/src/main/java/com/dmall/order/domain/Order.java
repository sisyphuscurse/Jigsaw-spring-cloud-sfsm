
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

    state = OrderStates.Paid;
    payment = new Payment(null, payment_id, oid, payment_time);

    sendEvent(OrderEvents.OrderPaid);
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
}
