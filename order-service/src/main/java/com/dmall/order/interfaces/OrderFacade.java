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
}
