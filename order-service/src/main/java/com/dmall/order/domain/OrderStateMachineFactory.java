package com.dmall.order.domain;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderStateMachineFactory {

  private StateMachineFactory<OrderStates, OrderEvents> factory;

  public OrderStateMachineFactory(StateMachineFactory<OrderStates, OrderEvents> factory) {
    this.factory = factory;
  }

  public StateMachine<OrderStates, OrderEvents> createAndStart(Integer oid) {

    StateMachine<OrderStates, OrderEvents> stateMachine = factory.getStateMachine(String.valueOf(oid));

    stateMachine.start();



    // TODO: 14/10/2017 persist to db

    return stateMachine;
  }

  public StateMachine<OrderStates, OrderEvents> get(Integer oid) {
    return null;
  }
}
