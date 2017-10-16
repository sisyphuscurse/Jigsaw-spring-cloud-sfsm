package com.dmall.order.interfaces.assembler;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderItem;
import com.dmall.order.interfaces.dto.CreateOrderRequest;
import com.dmall.order.interfaces.dto.CreateOrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderAssembler {
  public CreateOrderResponse toDTO(List<Order> orders) {
    return new CreateOrderResponse();
  }

  public Order toDomainObject(CreateOrderRequest request) {
    final Order order = new Order();
    order.setUid(request.getUid());
    order.setTotal_price(request.getTotal_price());
    order.setItems(toOrderItemList(request.getItems()));
    return order;
  }

  private List<OrderItem> toOrderItemList(List<CreateOrderRequest.OrderItem> items) {
    return items.stream()
        .map(this::toOrderItem)
        .collect(Collectors.toList());
  }

  private OrderItem toOrderItem(CreateOrderRequest.OrderItem orderItem) {
    return OrderItem.builder()
        //.id(orderItem)
        //.oid(orderItem)
        .pid(orderItem.getPid())
        .name(orderItem.getName())
        .price(orderItem.getPrice())
        .amount(orderItem.getAmount())
        .build();
  }

  public CreateOrderResponse toDTO(Order order) {
    return null;
  }
}
