package com.dmall.order.domain;

import java.util.List;

/**
 * Created by lianghuang on 27/09/2017.
 */
public interface OrderRepository {
  List<Order> getAllOrders();
}
