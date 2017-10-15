package com.dmall.order.domain;

public interface OrderRepository {

  Order getOrderById();

  Order save(Order order);
}
