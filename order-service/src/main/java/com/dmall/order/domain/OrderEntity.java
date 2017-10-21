
package com.dmall.order.domain;


import com.dmall.order.infrastructure.repository.OrderCancellationRepository;
import com.dmall.order.infrastructure.repository.OrderItemRepository;
import com.dmall.order.infrastructure.repository.OrderRepository;
import com.dmall.order.infrastructure.repository.PaymentRepository;
import com.dmall.order.infrastructure.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OrderEntity {

  public static final String ORDER_STATE_MACHINE = "orderStateMachine";

  private Order order;

  //TODO [Barry][SSM] 拿不掉的Boiler Plate: StateMachine
  private StateMachine<OrderStates, OrderEvents> stateMachine;

  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

  private OrderRepository repository;

  private OrderItemRepository orderItemRepository;

  private PaymentRepository paymentRepository;

  private ShipmentRepository shipmentRepository;

  private OrderCancellationRepository orderCancellationRepository;

  public OrderEntity(Order order, OrderStateMachineFactory stateMachineFactory, OrderRepository repository, OrderItemRepository orderItemRepository, PaymentRepository paymentRepository, ShipmentRepository shipmentRepository, OrderCancellationRepository orderCancellationRepository) {
    this.order = order;
    this.repository = repository;
    this.orderItemRepository = orderItemRepository;
    this.paymentRepository = paymentRepository;
    this.shipmentRepository = shipmentRepository;
    this.orderCancellationRepository = orderCancellationRepository;

    this.stateMachine = stateMachineFactory.build(this);
    this.stateMachine.start();
  }

  public OrderEntity(Integer oid, OrderStateMachineFactory stateMachineFactory, OrderRepository repository, OrderItemRepository orderItemRepository, PaymentRepository paymentRepository, ShipmentRepository shipmentRepository, OrderCancellationRepository orderCancellationRepository) {
    this.repository = repository;
    this.orderItemRepository = orderItemRepository;
    this.paymentRepository = paymentRepository;
    this.shipmentRepository = shipmentRepository;
    this.orderCancellationRepository = orderCancellationRepository;

    this.order = getOrderById(oid);
    this.stateMachine = stateMachineFactory.build(this);
    this.stateMachine.start();
  }

  //TODO [Barry][SSM] 拿不掉的Boiler Plate: StateMachineId
  public String getOrderStateMachineId() {
    return ORDER_STATE_MACHINE + order.getOid();
  }

  //TODO [Barry][SSM] 拿不掉的Boiler Plate: SendEvent Untyped
  public void sendEvent(Message<OrderEvents> message) {
    exceptionHandler(stateMachine.sendEvent(message));
  }

  //TODO [Barry][SSM] 拿不掉的Boiler Plate: SendEvent Typed
  public void sendEvent(OrderEvents events) {
    exceptionHandler(stateMachine.sendEvent(events));
  }

  private void exceptionHandler(boolean succeed) {
    if (!succeed) {
      throw new RuntimeException("The event cannot be matched.");
    } else if (stateMachine.hasStateMachineError()) {
      RuntimeException exception = (RuntimeException) stateMachine.getExtendedState().getVariables().get("ERROR");

      if (exception instanceof OrderDomainException) {
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
    order.setConfirm_time(simpleDateFormat.format(new Date()));
  }

  void onStateChanged() {
    order.setState(stateMachine.getState().getId());
    save();
  }

  void onError(Exception exception) {
    // TODO: 18/10/2017 The exception can't be fetch in orderEntity, Using ExtendedState save exception.
    stateMachine.getExtendedState().getVariables().put("ERROR", exception);
    stateMachine.setStateMachineError(exception);
  }

  public OrderStates getOrderState() {
    return order.getState();
  }

  public void onCancelled(String reason) {
    OrderCancellation orderCancellation = OrderCancellation.builder()
        .oid(this.order.getOid())
        .created_time(simpleDateFormat.format(new Date()))
        .reason(reason)
        .build();

    order.setOrderCancellation(orderCancellation);
  }

  public Order getOrder() {
    return order;
  }

  public Order save() {

    repository.save(order);

    if (Objects.nonNull(order.getItems())) {
      order.getItems().stream()
          .forEach(c -> c.setOid(order.getOid()));

      orderItemRepository.save(order.getItems());
    }

    if (Objects.nonNull(order.getPayment())) {
      paymentRepository.save(order.getPayment());
    }

    if (Objects.nonNull(order.getShipment())) {
      shipmentRepository.save(order.getShipment());
    }

    if (Objects.nonNull(order.getOrderCancellation())) {
      orderCancellationRepository.save(order.getOrderCancellation());
    }

    return order;
  }

  private Order getOrderById(Integer oid) {
    Order order = repository.findOne(oid);

    if (Objects.nonNull(order)) {
      order.setPayment(paymentRepository.findByOid(oid));
      order.setShipment(shipmentRepository.findByOid(oid));
      order.setItems(orderItemRepository.findByOid(oid));
      order.setOrderCancellation(orderCancellationRepository.findByOid(oid));
    }

    return order;
  }
}
