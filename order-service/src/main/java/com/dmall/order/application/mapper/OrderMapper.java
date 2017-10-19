package com.dmall.order.application.mapper;

import com.dmall.order.domain.Order;
import com.dmall.order.dto.OrderDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

  private ModelMapper modelMapper = new ModelMapper();

  public Order fromDto(OrderDTO orderDTO) {

    return modelMapper.map(orderDTO, Order.class);
  }

  public OrderDTO toDto(Order order) {

    return modelMapper.map(order, OrderDTO.class);
  }
}
