package com.dmall.order.api.mapper;

import com.dmall.order.api.request.CreateOrderRequest;
import com.dmall.order.api.response.OrderResponse;
import com.dmall.order.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapper {

  @Autowired
  OrderItemDtoMapper orderItemDtoMapper;

  public OrderDTO fromApi(CreateOrderRequest request) {
    final OrderDTO order = new OrderDTO();
    order.setUid(request.getUid());
    order.setTotal_price(request.getTotal_price());
    order.setItems(orderItemDtoMapper.fromApi(request.getItems()));
    return order;
  }

  public OrderResponse toApi(OrderDTO order) {
    return OrderResponse.builder()
        .oid(order.getOid())
        .total(order.getTotal_price())
        .items(orderItemDtoMapper.toApi(order.getItems()))
        .build();
  }
}
