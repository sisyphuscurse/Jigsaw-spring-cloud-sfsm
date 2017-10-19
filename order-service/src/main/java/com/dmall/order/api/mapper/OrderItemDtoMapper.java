package com.dmall.order.api.mapper;

import com.dmall.order.api.request.CreateOrderRequest;
import com.dmall.order.api.response.OrderResponse;
import com.dmall.order.dto.OrderItemDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemDtoMapper {

  public List<OrderItemDto> fromApi(List<CreateOrderRequest.OrderItem> items) {
    return items.stream()
        .map(this::fromApi)
        .collect(Collectors.toList());
  }

  public OrderItemDto fromApi(CreateOrderRequest.OrderItem orderItem) {
    return OrderItemDto.builder()
        //.id(orderItem)
        //.oid(orderItem)
        .pid(orderItem.getPid())
        .name(orderItem.getName())
        .price(orderItem.getPrice())
        .amount(orderItem.getAmount())
        .build();
  }

  public List<OrderResponse.OrderItem> toApi(List<OrderItemDto> items) {
    return items.stream()
        .map(this::toApi)
        .collect(Collectors.toList());
  }

  public OrderResponse.OrderItem toApi(OrderItemDto orderItem) {
    return OrderResponse.OrderItem.builder()
        //.id(orderItem)
        //.oid(orderItem)
        .pid(orderItem.getPid())
        .name(orderItem.getName())
        .price(orderItem.getPrice())
        .amount(orderItem.getAmount())
        .build();
  }
}
