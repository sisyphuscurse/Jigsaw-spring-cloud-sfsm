package com.dmall.order.domain;

public interface OrderRepository {

  Order getOrderById(Integer oid);

  Order save(Order order);

  void notifyPaid(Order order);

  void notifyShipped(Order order);

  void notifyReceived(Order order);
}
