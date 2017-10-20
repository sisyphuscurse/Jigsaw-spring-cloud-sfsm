
package com.dmall.order.domain;


import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;

import java.text.SimpleDateFormat;
import java.util.Date;

//TODO [Barry] 之所以封装这个层次的领域对象，就是为了赋予Spring Injection的能力，
// 如果他不是一个Spring Component，那么他就没有存在价值。
// 作为聚合根，Order这个领域对象应该负责组合其他领域对象。类似的Order JPA实体也会组合其他领域对象。
public class OrderEntity {

  public static final String ORDER_STATE_MACHINE = "orderStateMachine";

  //TODO [Barry] 这个order应该是Order领域对象对外部隐藏的JPA Entity
  private Order order;

  //TODO [Barry][SSM] 拿不掉的Boiler Plate: StateMachine
  private StateMachine<OrderStates, OrderEvents> stateMachine;

  private IOrderRepository repository;
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

  public OrderEntity(Order order, IOrderRepository repository) {
    this.order = order;
    this.repository = repository;
  }

  // TODO: 18/10/2017 [BOILDER PLATE] This method should be refectory
  public void initialize(StateMachine<OrderStates, OrderEvents> stateMachine) {
    this.stateMachine = stateMachine;
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
}
