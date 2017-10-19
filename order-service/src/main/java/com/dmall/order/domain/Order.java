
package com.dmall.order.domain;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Order {

  public static final String ORDER_STATE_MACHINE = "orderStateMachine";

  private Integer oid;

  private Integer uid;

  private BigDecimal total_price;

  private String create_time;

  private OrderStates state;

  private String confirm_time;

  private OrderCancellation orderCancellation;

  private List<OrderItem> items;

  private Payment payment;

  private Shipment shipment;

  public Order() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String current_datetime = format.format(new Date());
    this.setCreate_time(current_datetime);
    this.setState(OrderStates.Created);
  }

}
