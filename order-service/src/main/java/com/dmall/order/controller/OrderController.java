package com.dmall.order.controller;

import com.dmall.order.interfaces.dto.OrderRequest;
import com.dmall.order.interfaces.dto.OrderResponse;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/")
public class OrderController {

  @RequestMapping(value = "orders", method = RequestMethod.POST, headers = "Accept=application/json")
  public OrderResponse create_new_order(OrderRequest request) {

    OrderResponse.OrderItem item = OrderResponse.OrderItem.builder()
        .pid(1)
        .name("iPhone8")
        .amount(1)
        .price(BigDecimal.valueOf(8848.00))
        .build();
    List<OrderResponse.OrderItem> orderItems = Lists.newArrayList(item);
    return OrderResponse.builder()
        .oid(1)
        .total(BigDecimal.valueOf(8848.00))
        .create_time("2017-10-10 13:00")
        .status("Created")
        .items(orderItems)
        .build();
  }

  @RequestMapping(value = "orders/{oid}", method = RequestMethod.GET, headers = "Accept=application/json")
  public OrderResponse get_current_order(@PathVariable("oid") Integer oid) {

    OrderResponse.OrderItem item = OrderResponse.OrderItem.builder()
        .pid(1)
        .name("iPhone8")
        .amount(1)
        .price(BigDecimal.valueOf(8848.00))
        .build();
    List<OrderResponse.OrderItem> orderItems = Lists.newArrayList(item);
    return OrderResponse.builder()
        .oid(1)
        .total(BigDecimal.valueOf(8848.00))
        .create_time("2017-10-10 13:00")
        .status("Created")
        .items(orderItems)
        .build();
  }


  @RequestMapping(value = "orders/{oid}/set-paid", method = RequestMethod.POST, headers = "Accept=application/json")
  public void setPaid(@PathVariable("oid") Integer oid, String payment_id, String payment_time) {

  }

  @RequestMapping(value = "/orders/{oid}/deliver", method = RequestMethod.POST, headers = "Accept=application/json")
  public void deliver(@PathVariable("oid") Integer oid, String shipping_id, String shipments_time) {

  }


  @RequestMapping(value = "/orders/{oid}/confirm", method = RequestMethod.POST, headers = "Accept=application/json")
  public void confirm(@PathVariable("oid") Integer oid, String uid) {

  }

  @RequestMapping(value = "/orders/{oid}/cancel", method = RequestMethod.POST, headers = "Accept=application/json")
  public void cancel(@PathVariable("oid") Integer oid, String uid, String reason) {

  }
}


