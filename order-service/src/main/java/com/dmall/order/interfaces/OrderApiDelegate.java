package com.dmall.order.interfaces;

import com.dmall.order.interfaces.dto.OrderRequest;
import com.dmall.order.interfaces.dto.OrderResponse;
import org.springframework.http.ResponseEntity;

/**
 * Created by lianghuang on 27/09/2017.
 */
public interface OrderApiDelegate {

  ResponseEntity<OrderResponse> getAllOrders();

  void createOrder(OrderRequest request);
}
