package com.dmall.order.application;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderBeanFactory;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.domain.OrderDAO;
import com.dmall.order.domain.OrderStates;
import com.dmall.order.interfaces.assembler.OrderAssembler;
import com.dmall.order.interfaces.dto.CreateOrderRequest;
import com.dmall.order.interfaces.dto.CreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  public static final String ORDER_STATE_MACHINE = "orderStateMachine";

  @Autowired
  private OrderAssembler orderAssembler;

  @Autowired
  private OrderDAO orderDAO;

  @Autowired
  private OrderBeanFactory orderBeanFactory;


  @Autowired
  public OrderService(StateMachineFactory<OrderStates, OrderEvents> factory, OrderDAO orderDAO) {
    this.orderDAO = orderDAO;
  }

  public CreateOrderResponse createOrder(CreateOrderRequest orderRequest) {
    final Order order = orderDAO.save(orderAssembler.toDomainObject(orderRequest));
    return orderAssembler.toDTO(order);
  }

  public Order getOrderById(Integer oid) {
    return orderDAO.getOrderById(oid);
  }

  public void notifyPaid(Integer oid, String payment_id, String payment_time) {
    orderBeanFactory.load(oid).notifyPaid(payment_id, payment_time);
  }


  public void notifyInDelivery(Integer oid, String shipping_id, String shipping_time) {
    orderBeanFactory.load(oid).notifyInDelivery(shipping_id, shipping_time);
  }

  public void notifyReceived(Integer oid, Integer shipping_id, String receive_time) {
    orderBeanFactory.load(oid).notifyReceived(shipping_id, receive_time);
  }

  public void confirmOrder(Integer oid, String uid) {
    orderBeanFactory.load(oid).confirm();
  }
}
