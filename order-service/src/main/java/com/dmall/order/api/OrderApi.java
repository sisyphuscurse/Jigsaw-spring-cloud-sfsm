package com.dmall.order.api;


import com.dmall.order.api.mapper.OrderDtoMapper;
import com.dmall.order.api.request.CreateOrderRequest;
import com.dmall.order.api.response.OrderResponse;
import com.dmall.order.application.OrderService;
import com.dmall.order.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class OrderApi {

  @Autowired
  private OrderDtoMapper orderDtoMapper;

  @Autowired
  private OrderService orderService;

  @Autowired
  public OrderApi(OrderService orderService) {
    this.orderService = orderService;
  }

  @RequestMapping(value = "orders", method = RequestMethod.POST, headers = "Accept=application/json")
  public OrderResponse create_new_order(CreateOrderRequest request) {
    final OrderDto order = orderService.createOrder(orderDtoMapper.fromApi(request));
    return orderDtoMapper.toApi(order);
  }

  @RequestMapping(value = "orders/{oid}", method = RequestMethod.GET, headers = "Accept=application/json")
  public OrderResponse get_current_order(@PathVariable("oid") Integer oid) {
    //return orderDtoMapper.toApi(orderService.getOrderById(oid));

    return null;
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
  public void cancel(@PathVariable("oid") Integer oid, Integer uid, String reason) {
    orderService.cancelOrder(oid, uid, reason);
  }
}


