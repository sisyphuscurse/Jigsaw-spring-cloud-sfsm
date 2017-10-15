package com.dmall.order.domain;

public interface OrderRepository {

  Order getOrderById();

  Integer save(Order order);
}
