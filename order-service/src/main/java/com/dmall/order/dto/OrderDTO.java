
package com.dmall.order.dto;


import com.dmall.order.domain.OrderStates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderDTO {


  private Integer oid;

  private Integer uid;

  private BigDecimal total_price;

  private String create_time;

  private OrderStates state;

  private String confirm_time;

  private OrderCancellationDTO orderCancellation;

  private List<OrderItemDTO> items;

  private PaymentDTO payment;

  private ShipmentDTO shipment;

  public OrderDTO() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String current_datetime = format.format(new Date());
    this.setCreate_time(current_datetime);
    this.setState(OrderStates.Created);
  }

}
