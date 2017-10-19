
package com.dmall.order.dto;


import com.dmall.order.domain.OrderStates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class OrderDto {

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
  private OrderCancellationDto orderCancellation;

  @Transient
  private List<OrderItemDto> items;

  @Transient
  private PaymentDto payment;

  @Transient
  private ShipmentDto shipment;

  public OrderDto() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String current_datetime = format.format(new Date());
    this.setCreate_time(current_datetime);
    this.setState(OrderStates.Created);
  }

}
