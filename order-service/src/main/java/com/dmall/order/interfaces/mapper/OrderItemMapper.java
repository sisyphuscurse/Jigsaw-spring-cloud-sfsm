package com.dmall.order.interfaces.mapper;

import com.dmall.order.domain.OrderItem;
import com.dmall.order.interfaces.dto.CreateOrderRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {

  public List<OrderItem> fromApi(List<CreateOrderRequest.OrderItem> items) {
    return items.stream()
        .map(this::fromApi)
        .collect(Collectors.toList());
  }

  public OrderItem fromApi(CreateOrderRequest.OrderItem orderItem) {
    return OrderItem.builder()
        //.id(orderItem)
        //.oid(orderItem)
        .pid(orderItem.getPid())
        .name(orderItem.getName())
        .price(orderItem.getPrice())
        .amount(orderItem.getAmount())
        .build();
  }

}
