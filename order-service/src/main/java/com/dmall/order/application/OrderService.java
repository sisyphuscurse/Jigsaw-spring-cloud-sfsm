package com.dmall.order.application;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderRepository;

import java.util.List;

/**
 * Created by lianghuang on 27/09/2017.
 */
public class OrderService {

  private OrderRepository repository;


  public OrderService(OrderRepository repository) {
    this.repository = repository;
  }


  public List<Order> getAllOrders() {
    return repository.getAllOrders();
  }
}
