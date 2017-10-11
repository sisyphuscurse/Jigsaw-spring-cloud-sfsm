package com.dmall.order.controller;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.Product;
import com.dmall.order.domain.Shipping;
import com.dmall.order.infrastructure.broker.ProductBroker;
import com.dmall.order.infrastructure.broker.ShippingBroker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/")
public class OrderController {

  @Autowired
  private ProductBroker productBroker;


  @Autowired
  private ShippingBroker shippingBroker;

  private List<Order> orders = Arrays.asList(
      new Order("o001", "p001", "g001"),
      new Order("o002", "p002", "g002"));

  public OrderController() throws ParseException {

  }

  @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
  public List<Order> getOrders() {
    return orders;
  }

  @RequestMapping(value = "{orderId}", method = RequestMethod.GET, headers = "Accept=application/json")
  public Order getTaskByTaskId(@PathVariable("orderId") String orderId) {
    Order orderToReturn = null;
    for (Order order : orders) {
      if (order.getOrderId().equalsIgnoreCase(orderId)) {
        orderToReturn = order;
        break;
      }
    }

    if (orderToReturn != null) {
      Product product = this.productBroker.getProductDetial(orderToReturn.getProductId());
      orderToReturn.setProduct(product);
      Shipping shipping = shippingBroker.getShippingDetail(orderToReturn.getGoodsId());
      orderToReturn.setShipping(shipping);
    }

    return orderToReturn;
  }


  @RequestMapping(value = "shippings/{goodsId}", method = RequestMethod.GET, headers = "Accept=application/json")
  public Shipping getPatientById(@PathVariable("goodsId") String goodsId) {

    return shippingBroker.getShippingDetail(goodsId);
  }
}
