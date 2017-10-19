package com.dmall.order.application.mapper;

import com.dmall.order.domain.Order;
import com.dmall.order.dto.OrderDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

  private ModelMapper modelMapper = new ModelMapper();

  @Autowired
  private OrderItemMapper orderItemMapper;

  public Order fromDto(OrderDto orderDto) {

    return modelMapper.map(orderDto, Order.class);
//
//    final Order order = new Order();
//    order.setUid(orderDto.getUid());
//    order.setTotal_price(orderDto.getTotal_price());
//    order.setItems(orderItemMapper.fromDto(orderDto.getItems()));
//    return order;
  }

  public OrderDto toDto(Order order) {

    return modelMapper.map(order, OrderDto.class);
//    return OrderDto.builder()
//        .oid(order.getOid())
//        .total_price(order.getTotal_price())
//        .items(orderItemMapper.toDto(order.getItems()))
//        .build();
  }
}
