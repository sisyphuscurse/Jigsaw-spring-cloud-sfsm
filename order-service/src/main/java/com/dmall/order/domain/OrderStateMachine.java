package com.dmall.order.domain;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.annotation.EventHeaders;
import org.springframework.statemachine.annotation.OnStateChanged;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.web.context.WebApplicationContext;

import java.util.EnumSet;
import java.util.Map;

@Configuration
@EnableStateMachineFactory
@Slf4j
@WithStateMachine(id = "orderStateMachine")
public class OrderStateMachine extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {

  @Autowired
  private WebApplicationContext context;

  @Override
  public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception {
    config
        .withConfiguration()
        .machineId("orderStateMachine");
  }

  @Override
  public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
    states
        .withStates()
        .initial(OrderStates.Idle)
        .states(EnumSet.allOf(OrderStates.class));
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
    transitions
        .withExternal()
        .source(OrderStates.Idle).target(OrderStates.Created)
        .event(OrderEvents.OrderCreated)
        .and()
        .withExternal()
        .source(OrderStates.Created).target(OrderStates.Paid)
        .event(OrderEvents.OrderPaid)
        .and()
        .withExternal()
        .source(OrderStates.Paid).target(OrderStates.InDelivery)
        .event(OrderEvents.OrderShipped)
        .and()
        .withExternal()
        .source(OrderStates.InDelivery).target(OrderStates.Received)
        .event(OrderEvents.OrderShipped)
        .and()
        .withExternal()
        .source(OrderStates.Received).target(OrderStates.Confirmed)
        .event(OrderEvents.OrderConfirmed)
        .and()
        .withExternal()
        .source(OrderStates.Created).target(OrderStates.Cancelled)
        .event(OrderEvents.OrderCancelled);
  }


  @OnTransition(source = "Idle", target = "Created")
  public void createOrder(@EventHeaders Map<String, Object> headers, ExtendedState extendedState, StateMachine<String, String> stateMachine) {
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

  @OnTransition(target = "Received")
  public void updateToReceived() {
  }

  @OnTransition(target = "Confirmed")
  public void confirm() {

  }

  @OnTransition(target = "Cancelled")
  public void cancelOrder() {
  }

  @OnStateChanged
  public void onStateChanged(@EventHeaders Map<String, Object> headers, ExtendedState extendedState, StateMachine<OrderStates, OrderEvents> stateMachine) {

    if (stateMachine.getState() != stateMachine.getInitialState()) {
      StateMachinePersist<OrderStates, OrderEvents, String> orderStatemachinePersist = context.getBean(StateMachinePersist.class);
      StateMachinePersister<OrderStates, OrderEvents, String> persister = new DefaultStateMachinePersister<>(orderStatemachinePersist);
      Order order = (Order) headers.get("order");
      try {
        persister.persist(stateMachine, String.valueOf(order.getOid()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
