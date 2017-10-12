
package com.dmall.order.domain;


import com.dmall.order.infrastructure.broker.Product;
import com.dmall.order.infrastructure.broker.Shipping;
import com.dmall.order.interfaces.dto.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

import java.math.BigDecimal;
import java.util.List;

@WithStateMachine
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  private Integer oid;
  private Integer uid;
  private BigDecimal total;
  private List<OrderItem> items;
  private String create_time;
  private String status;

  @OnTransition(target = "Created")
  void createOrder() {
  }

  @OnTransition(target = "Cancelled")
  void cancelOrder() {
  }

  @OnTransition(target = "Paid")
  void updateToPaid() {
  }

  @OnTransition(target = "InDelivery")
  void updateToInDelivery() {
  }

  @OnTransition(target = "Received")
  void updateToReceived() {
  }

  @OnTransition(target = "Confirmed")
  void confirm() {
  }
}
