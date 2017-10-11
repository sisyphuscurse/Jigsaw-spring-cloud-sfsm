package com.dmall.order.interfaces.assembler;

import com.dmall.order.domain.Order;
import com.dmall.order.interfaces.dto.OrderResponse;

import java.util.List;

/**
 * Created by azhu on 17/09/2017.
 */
public class OrderAssembler {
  public OrderResponse toDTO(List<Order> orders) {
    return new OrderResponse();
  }
}
