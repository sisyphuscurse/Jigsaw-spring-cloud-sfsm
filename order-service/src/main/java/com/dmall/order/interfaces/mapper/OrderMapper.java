package com.dmall.order.interfaces.mapper;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderItem;
import com.dmall.order.interfaces.dto.CreateOrderRequest;
import com.dmall.order.interfaces.dto.CreateOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

  @Autowired
  OrderItemMapper orderItemMapper;

  public Order fromApi(CreateOrderRequest request) {
    final Order order = new Order();
    order.setUid(request.getUid());
    order.setTotal_price(request.getTotal_price());
    order.setItems(orderItemMapper.fromApi(request.getItems()));
    return order;
  }

  public CreateOrderResponse toApi(Order order) {
    return null;
  }
}
