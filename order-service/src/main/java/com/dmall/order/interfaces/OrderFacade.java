package com.dmall.order.interfaces;

import com.dmall.order.application.OrderService;
import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.domain.OrderStates;
import com.dmall.order.interfaces.assembler.OrderAssembler;
import com.dmall.order.interfaces.dto.OrderRequest;
import com.dmall.order.interfaces.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFacade implements OrderApiDelegate {

  @Autowired
  private StateMachineFactory<OrderStates, OrderEvents> factory;


  @Autowired
  OrderService orderService;

  private OrderAssembler orderAssembler = new OrderAssembler();

  @Override
  public ResponseEntity<OrderResponse> getAllOrders() {
    List<Order> order = null;//orderService.getAllOrders();
    return new ResponseEntity<OrderResponse>(orderAssembler.toDTO(order), HttpStatus.CREATED);
  }

  @Override
  public void createOrder(OrderRequest request) {
    Order order = orderAssembler.toDomainObject(request);
    orderService.createOrder(order);
  }

  @Override
  public void notifyPaid(Integer oid, String payment_id, String payment_time) {
    orderService.notifyPaid(oid, payment_id, payment_time);
  }

  @Override
  public void notifyInDelivery(Integer oid, String shipping_id, String shipping_time) {
    orderService.notifyInDelivery(oid, shipping_id, shipping_time);

  }

  @Override
  public void notifyReceivd(Integer oid, Integer shipping_id, String receive_time) {
    orderService.notifyReceived(oid, shipping_id, receive_time);

  }
}
