package com.dmall.order.application.mapper;

import com.dmall.order.domain.Order;
import com.dmall.order.dto.OrderDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

  private ModelMapper modelMapper = new ModelMapper();

  @Autowired
  private OrderItemMapper orderItemMapper;

  public Order fromDto(OrderDTO orderDTO) {

    return modelMapper.map(orderDTO, Order.class);
//
//    final Order order = new Order();
//    order.setUid(orderDTO.getUid());
//    order.setTotal_price(orderDTO.getTotal_price());
//    order.setItems(orderItemMapper.fromDto(orderDTO.getItems()));
//    return order;
  }

  public OrderDTO toDto(Order order) {

    return modelMapper.map(order, OrderDTO.class);
//    return OrderDTO.builder()
//        .oid(order.getOid())
//        .total_price(order.getTotal_price())
//        .items(orderItemMapper.toDto(order.getItems()))
//        .build();
  }
}
