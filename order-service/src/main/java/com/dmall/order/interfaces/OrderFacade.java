package com.dmall.order.interfaces;

import com.dmall.order.application.OrderService;
import com.dmall.order.domain.Order;
import com.dmall.order.interfaces.assembler.OrderAssembler;
import com.dmall.order.interfaces.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by azhu on 17/09/2017.
 */
public class OrderFacade implements OrderApiDelegate {

  @Autowired
  OrderService orderService;

  private OrderAssembler orderAssembler = new OrderAssembler();

  @Override
  public ResponseEntity<OrderResponse> getAllOrders() {
    List<Order> order = orderService.getAllOrders();
    return new ResponseEntity<OrderResponse>(orderAssembler.toDTO(order), HttpStatus.CREATED);
  }
}
