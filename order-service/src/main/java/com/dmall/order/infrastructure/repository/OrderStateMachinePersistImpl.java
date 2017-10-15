package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.OrderEvents;
import com.dmall.order.domain.OrderStates;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.stereotype.Repository;

import java.util.HashMap;


@Repository
public class OrderStateMachinePersistImpl implements StateMachinePersist<OrderStates, OrderEvents, String> {

  private final HashMap<String, StateMachineContext<OrderStates, OrderEvents>> contexts = new HashMap<>();

  @Override
  public void write(StateMachineContext<OrderStates, OrderEvents> context, String contextObj) throws Exception {
    context.toString();
    contexts.put(contextObj, context);
  }

  @Override
  public StateMachineContext<OrderStates, OrderEvents> read(String contextObj) throws Exception {
    return contexts.get(contextObj);
  }
}
