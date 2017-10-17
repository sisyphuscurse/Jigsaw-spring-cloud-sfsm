
package com.dmall.order.domain;


import com.dmall.order.infrastructure.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;

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
@Entity
@Table(name = "orders")
public class Order {

  public static final String ORDER_STATE_MACHINE = "orderStateMachine";

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

  @Column(nullable = false)
  private String confirm_time;

  @Transient
  private List<OrderItem> items;

  @Transient
  private Payment payment;

  @Transient
  private Shipment shipment;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @Transient
  private StateMachine<OrderStates, OrderEvents> stateMachine;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @Transient
  private OrderRepository repository;

  public Order() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String current_datetime = format.format(new Date());
    this.setCreate_time(current_datetime);
    this.setState(OrderStates.Created);
  }

  public void initialize(StateMachine<OrderStates, OrderEvents> stateMachine, OrderRepository repository) {
    this.stateMachine = stateMachine;
    this.repository = repository;

    this.stateMachine.start();
  }

  public String getOrderStateMachineId() {
    return ORDER_STATE_MACHINE + oid;
  }

  public void sendEvent(Message<OrderEvents> message) {
    stateMachine.sendEvent(message);
  }

  public void sendEvent(OrderEvents events) {
    stateMachine.sendEvent(events);
  }

  void onPaid(String payment_id, String payment_time) {
    payment = new Payment(null, payment_id, oid, payment_time);
  }

  void onShipped(String shipping_id, String shipping_time) {
    shipment = new Shipment(null, shipping_id, oid, shipping_time, null);
  }

  void onReceived(String receivedTime) {
    shipment.setReceived_time(receivedTime);
  }

  void onConfirmed() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    confirm_time = format.format(new Date());
  }

  void onStateChanged() {
    state = stateMachine.getState().getId();
    repository.save(this);
  }
}
