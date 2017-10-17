package com.dmall.order.domain;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

import static com.dmall.order.application.OrderService.ORDER_STATE_MACHINE;

//@Slf4j
@Component("orderEntity")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrderEntity extends StateMachineListenerAdapter<OrderStates, OrderEvents> {

  private ApplicationContext context;


  private final Order order;
  private StateMachine<OrderStates, OrderEvents> stateMachine;

  public OrderEntity() {
    this.order = new Order();
    this.order.setState(OrderStates.Created);
    this.stateMachine = buildStateMachine();
  }

  public OrderEntity(ApplicationContext context, Order order) {
    this.context = context;
    this.order = order;

    this.stateMachine = buildStateMachine();

    restoreStateMachine();
  }

  private void restoreStateMachine() {
    StateMachineAccess<OrderStates, OrderEvents> region = stateMachine.getStateMachineAccessor().withRegion();
    region.resetStateMachine(
        new DefaultStateMachineContext<>(order.getState(), null, null, null, null, buildStateMachineId()));
    this.stateMachine.start();
  }

  private String buildStateMachineId() {
    return ORDER_STATE_MACHINE;
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

  @Override
  public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
    order.setState(stateMachine.getState().getId());
    getOrderRepository().save(order);
    super.stateChanged(from, to);
  }


  public OrderDAO getOrderRepository() {
    return context.getBean(OrderDAO.class);
  }

  private StateMachine<OrderStates, OrderEvents> buildStateMachine() {

    StateMachineBuilder.Builder<OrderStates, OrderEvents> builder = StateMachineBuilder.builder();

    try {
      builder.configureConfiguration()
          .withConfiguration()
          .machineId("myMachineId")
          .listener(this)
          .beanFactory(new StaticListableBeanFactory());

      builder.configureStates()
          .withStates()
          .initial(OrderStates.Created)
          .states(EnumSet.allOf(OrderStates.class));

      builder.configureTransitions()
          .withExternal()
          .source(OrderStates.Created).target(OrderStates.Paid)
          .event(OrderEvents.OrderPaid)
          .action(notifyPaidAction())
          .and()
          .withExternal()
          .source(OrderStates.Paid).target(OrderStates.InDelivery)
          .event(OrderEvents.OrderShipped)
          .action(notifyShippedAction())
          .and()
          .withExternal()
          .source(OrderStates.InDelivery).target(OrderStates.Received)
          .event(OrderEvents.OrderReceived)
          .action(notifyReceivedAction())
          .and()
          .withExternal()
          .source(OrderStates.Received).target(OrderStates.Confirmed)
          .event(OrderEvents.OrderConfirmed)
          .action(confirmOrder())
          .and()
          .withExternal()
          .source(OrderStates.Created).target(OrderStates.Cancelled)
          .event(OrderEvents.OrderCancelled)
          .action(cancelOrder());
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return builder.build();
  }

  private Action<OrderStates, OrderEvents> notifyPaidAction() {
    return context -> {
      final MessageHeaders headers = context.getMessageHeaders();
      String payment_id = (String) headers.get("payment_id");
      String payment_time = (String) headers.get("payment_time");
      order.setPayment(new Payment(null, payment_id, order.getOid(), payment_time));
    };
  }

  private Action<OrderStates, OrderEvents> notifyShippedAction() {

    return context -> {
      final MessageHeaders headers = context.getMessageHeaders();
      String shipping_id = (String) headers.get("shipping_id");
      String shipping_time = (String) headers.get("shipping_time");

      order.setShipment(new Shipment(null, shipping_id, order.getOid(), shipping_time, null));
    };
  }

  private Action<OrderStates, OrderEvents> notifyReceivedAction() {

    return context -> {
      final MessageHeaders headers = context.getMessageHeaders();
      String receivedTime = (String) headers.get("received_time");
      order.getShipment().setReceived_time(receivedTime);
    };
  }


  private Action<OrderStates, OrderEvents> confirmOrder() {
    return context -> {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      order.setConfirm_time(format.format(new Date()));
    };
  }

  private Action<OrderStates, OrderEvents> cancelOrder() {
    return context -> {

    };
  }
}
