package com.dmall.order.application.mapper;

import com.dmall.order.domain.OrderItem;
import com.dmall.order.dto.OrderItemDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {

  public List<OrderItem> fromDto(List<OrderItemDto> items) {
    return items.stream()
        .map(this::fromDto)
        .collect(Collectors.toList());
  }

  public OrderItem fromDto(OrderItemDto orderItem) {
    return OrderItem.builder()
        //.id(orderItem)
        //.oid(orderItem)
        .pid(orderItem.getPid())
        .name(orderItem.getName())
        .price(orderItem.getPrice())
        .amount(orderItem.getAmount())
        .build();
  }

  public List<OrderItemDto> toDto(List<OrderItem> items) {
    return items.stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public OrderItemDto toDto(OrderItem orderItem) {
    return OrderItemDto.builder()
        //.id(orderItem)
        //.oid(orderItem)
        .pid(orderItem.getPid())
        .name(orderItem.getName())
        .price(orderItem.getPrice())
        .amount(orderItem.getAmount())
        .build();
  }
}
