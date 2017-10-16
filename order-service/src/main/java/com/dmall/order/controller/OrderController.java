package com.dmall.order.controller;

import com.dmall.order.domain.OrderDAO;
import com.dmall.order.application.OrderService;
import com.dmall.order.interfaces.dto.CreateOrderRequest;
import com.dmall.order.interfaces.dto.CreateOrderResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderDAO orderDAO;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @RequestMapping(value = "orders", method = RequestMethod.POST, headers = "Accept=application/json")
  public CreateOrderResponse create_new_order(CreateOrderRequest request) {

    orderService.createOrder(request);

    CreateOrderResponse.OrderItem item = CreateOrderResponse.OrderItem.builder()
        .pid(1)
        .name("iPhone8")
        .amount(1)
        .price(BigDecimal.valueOf(8848.00))
        .build();
    List<CreateOrderResponse.OrderItem> orderItems = Lists.newArrayList(item);

    return CreateOrderResponse.builder()
        .oid(1)
        .total(BigDecimal.valueOf(8848.00))
        .create_time("2017-10-10 13:00")
        .status("Created")
        .items(orderItems)
        .build();
  }

  @RequestMapping(value = "orders/{oid}", method = RequestMethod.GET, headers = "Accept=application/json")
  public CreateOrderResponse get_current_order(@PathVariable("oid") Integer oid) {

    CreateOrderResponse.OrderItem item = CreateOrderResponse.OrderItem.builder()
        .pid(1)
        .name("iPhone8")
        .amount(1)
        .price(BigDecimal.valueOf(8848.00))
        .build();
    List<CreateOrderResponse.OrderItem> orderItems = Lists.newArrayList(item);
    return CreateOrderResponse.builder()
        .oid(1)
        .total(BigDecimal.valueOf(8848.00))
        .create_time("2017-10-10 13:00")
        .status("Created")
        .items(orderItems)
        .build();
  }


  @RequestMapping(value = "orders/{oid}/set-paid", method = RequestMethod.POST, headers = "Accept=application/json")
  public void setPaid(@PathVariable("oid") Integer oid, String payment_id, String payment_time) {
      orderService.notifyPaid(oid, payment_id, payment_time);
  }

  @RequestMapping(value = "/orders/{oid}/deliver", method = RequestMethod.POST, headers = "Accept=application/json")
  public void deliver(@PathVariable("oid") Integer oid, String shipping_id, String shipments_time) {
    orderService.notifyInDelivery(oid, shipping_id, shipments_time);
  }

  @RequestMapping(value = "/orders/{oid}/set-received", method = RequestMethod.POST, headers = "Accept=application/json")
  public void receive(@PathVariable("oid") Integer oid, Integer shipping_id, String receive_time) {
    orderService.notifyReceived(oid, shipping_id, receive_time);
  }

  @RequestMapping(value = "/orders/{oid}/confirm", method = RequestMethod.POST, headers = "Accept=application/json")
  public void confirm(@PathVariable("oid") Integer oid, String uid) {
    orderService.confirmOrder(oid, uid);
  }

  @RequestMapping(value = "/orders/{oid}/cancel", method = RequestMethod.POST, headers = "Accept=application/json")
  public void cancel(@PathVariable("oid") Integer oid, String uid, String reason) {

  }
}


