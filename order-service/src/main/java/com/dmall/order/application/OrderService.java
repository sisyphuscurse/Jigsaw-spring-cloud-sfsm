package com.dmall.order.application;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.domain.OrderRepository;
import com.dmall.order.domain.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

  public static final String ORDER_STATE_MACHINE = "orderStateMachine";
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

    StateMachine<OrderStates, OrderEvents> stateMachine = factory.getStateMachine(ORDER_STATE_MACHINE);

    order.installStateMachine(stateMachine);

    order.notifyOrderCreated();
  }

  public Order getOrderById(Integer oid) {
    return repository.getOrderById(oid);
  }

  public void notifyPaid(Integer oid, String payment_id, String payment_time) {

    Order order = prepareInstance(oid);

    order.notifyPaid(payment_id, payment_time);
  }


  public void notifyInDelivery(Integer oid, String shipping_id, String shipping_time) {
    Order order = prepareInstance(oid);

    order.notifyInDelivery(shipping_id, shipping_time);
  }

  private Order prepareInstance(Integer oid) {
    Order order = repository.getOrderById(oid);
    if (Objects.nonNull(oid)) {

      StateMachine<OrderStates, OrderEvents> stateMachine = factory.getStateMachine(ORDER_STATE_MACHINE);
      List<StateMachineAccess<OrderStates, OrderEvents>> withAllRegions = stateMachine.getStateMachineAccessor().withAllRegions();
      for (StateMachineAccess<OrderStates, OrderEvents> region : withAllRegions) {
        region.resetStateMachine(
            new DefaultStateMachineContext<>(order.getState(),null, null, null, null, ORDER_STATE_MACHINE));
      }
      stateMachine.start();

      order.setStateMachine(stateMachine);

      return order;
    }
    return null;
  }

  public void notifyReceived(Integer oid, Integer shipping_id, String receive_time) {
    Order order = prepareInstance(oid);
    order.notifyReceived(shipping_id, receive_time);

  }
}
