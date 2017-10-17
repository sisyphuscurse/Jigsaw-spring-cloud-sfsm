package com.dmall.order.domain;

public interface IOrderRepository {

  Order getOrderById(Integer oid);

  Order save(Order order);
}
