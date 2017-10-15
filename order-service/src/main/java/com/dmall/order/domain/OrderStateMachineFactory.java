package com.dmall.order.domain;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

@Component
public class OrderStateMachineFactory {

  private StateMachineFactory<OrderStates, OrderEvents> factory;

  private final StateMachinePersister<OrderStates, OrderEvents, String> persister;

  public OrderStateMachineFactory(StateMachineFactory<OrderStates, OrderEvents> factory, StateMachinePersist<OrderStates, OrderEvents, String> orderStatemachinePersist) {
    this.factory = factory;
    this.persister = new DefaultStateMachinePersister<>(orderStatemachinePersist);
  }

  public StateMachine<OrderStates, OrderEvents> newStatemachine(String machineId) {

    StateMachine<OrderStates, OrderEvents> stateMachine = factory.getStateMachine(machineId);

    return stateMachine;
  }

  public StateMachine<OrderStates, OrderEvents> get(Integer oid)  {

    StateMachine<OrderStates, OrderEvents> stateMachine = factory.getStateMachine(String.valueOf(oid));

    try {
      return persister.restore(stateMachine, String.valueOf(oid));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
