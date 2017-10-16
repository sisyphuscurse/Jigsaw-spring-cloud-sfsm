package com.dmall.order.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class OrderBeanFactory {
  @Autowired
  private ApplicationContext context;
  @Autowired
  private OrderDAO orderRepository;

  public OrderEntity load(Integer oid) {
    return (OrderEntity) context.getBean("orderEntity", context, orderRepository.getOrderById(oid));
  }
}
