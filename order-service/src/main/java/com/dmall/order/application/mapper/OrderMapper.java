package com.dmall.order.application.mapper;

import com.dmall.order.domain.Order;
import com.dmall.order.dto.OrderDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//TODO [Barry] 没有看出OrderMapper对ModelMapper封装的价值. [Leo]在两个对象是一一对应的情况下，这个封装不是必要的，但是要是有一些属性不一致需要在这个类中做映射关系逻辑
public class OrderMapper {

  private ModelMapper modelMapper = new ModelMapper();

  public Order fromDto(OrderDTO orderDTO) {

    return modelMapper.map(orderDTO, Order.class);
  }

  public OrderDTO toDto(Order order) {

    return modelMapper.map(order, OrderDTO.class);
  }
}
