package com.dmall.order.interfaces.assembler;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderItem;
import com.dmall.order.interfaces.dto.OrderRequest;
import com.dmall.order.interfaces.dto.OrderResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by azhu on 17/09/2017.
 */
public class OrderAssembler {
  public OrderResponse toDTO(List<Order> orders) {
    return new OrderResponse();
  }

  public Order toDomainObject(OrderRequest request) {
    return Order.builder()
        .uid(request.getUid())
        .total_price(request.getTotal_price())
        .items(toOrderItemList(request.getItems()))
        .build();
  }

  private List<OrderItem> toOrderItemList(List<OrderRequest.OrderItem> items) {
    return items.stream()
        .map(this::toOrderItem)
        .collect(Collectors.toList());
  }

  private OrderItem toOrderItem(OrderRequest.OrderItem orderItem) {
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
