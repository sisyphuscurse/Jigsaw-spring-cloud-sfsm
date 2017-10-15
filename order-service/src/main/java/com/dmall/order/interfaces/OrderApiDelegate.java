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

  void notifyPaid(Integer oid, String payment_id, String payment_time);

  void notifyInDelivery(Integer oid, String shipping_id, String shipping_time);

  void notifyReceivd(Integer oid, Integer shipping_id, String receive_time);
}
