
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
@Entity
@Builder
@AllArgsConstructor
@Table(name = "orders")
public class OrderDTO {

  public static final String ORDER_STATE_MACHINE = "orderStateMachine";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer oid;

  @Column(nullable = false)
  private Integer uid;

  @Column(nullable = false)
  private BigDecimal total_price;

  @Column(nullable = false)
  private String create_time;

  @Enumerated(EnumType.STRING)
  private OrderStates state;

  @Column(nullable = false)
  private String confirm_time;

  @Transient
  private OrderCancellationDTO orderCancellation;

  @Transient
  private List<OrderItemDTO> items;

  @Transient
  private PaymentDTO payment;

  @Transient
  private ShipmentDTO shipment;

  public OrderDTO() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String current_datetime = format.format(new Date());
    this.setCreate_time(current_datetime);
    this.setState(OrderStates.Created);
  }

}
