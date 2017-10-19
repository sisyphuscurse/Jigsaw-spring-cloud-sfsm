package com.dmall.order.api.mapper;

import com.dmall.order.api.request.CreateOrderRequest;
import com.dmall.order.api.response.OrderResponse;
import com.dmall.order.dto.OrderDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapper {

  private ModelMapper modelMapper = new ModelMapper();

  public OrderDTO fromApi(CreateOrderRequest request) {

    return modelMapper.map(request, OrderDTO.class);

  }

  public OrderResponse toApi(OrderDTO order) {
    return modelMapper.map(order, OrderResponse.class);
  }
}
