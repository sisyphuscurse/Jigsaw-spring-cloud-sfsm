package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderRepository;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lianghuang on 27/09/2017.
 */
public class OrderRepositoryImpl implements OrderRepository {

  public List<Order> getAllOrders() {
    return Arrays.asList(
        new Order("o001", "p001", "g001"),
        new Order("o002", "p002", "g002"));
  }
}
