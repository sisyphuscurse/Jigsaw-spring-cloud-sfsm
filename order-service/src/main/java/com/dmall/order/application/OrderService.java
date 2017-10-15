package com.dmall.order.application;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.domain.OrderRepository;
import com.dmall.order.domain.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderService {

  private final StateMachineFactory<OrderStates, OrderEvents> factory;
  private OrderRepository repository;
  private StateMachinePersister<OrderStates, OrderEvents, String> persister;


  @Autowired
  public OrderService(StateMachineFactory<OrderStates, OrderEvents> factory, OrderRepository repository, StateMachinePersist<OrderStates, OrderEvents, String> orderStatemachinePersist) {
    this.factory = factory;
    this.repository = repository;
    this.persister = new DefaultStateMachinePersister<>(orderStatemachinePersist);
  }


  public void createOrder(Order order) {

    StateMachine<OrderStates, OrderEvents> stateMachine = factory.getStateMachine("orderStateMachine");

    order.installStateMachine(stateMachine);

    order.notifyOrderCreated();
  }

  public Order getOrderById(Integer oid) {
    return repository.getOrderById(oid);
  }

  public void setPaid(Integer oid, String payment_id, String payment_time) {

    Order order = prepareInstance(oid);

    order.notifyPaid(payment_id, payment_time);
  }

  private Order prepareInstance(Integer oid) {
    Order order = repository.getOrderById(oid);
    if (Objects.nonNull(oid)) {
      StateMachine<OrderStates, OrderEvents> stateMachine = factory.getStateMachine("orderStateMachine");
      try {
        persister.restore(stateMachine, String.valueOf(oid));
        order.setStateMachine(stateMachine);
        return order;
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    return null;
  }
}
