package com.dmall.order.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
  private Integer oid;
  private Integer uid;
  private BigDecimal total_price;
  private List<OrderItem> items;
  private String create_time;
  private String status;

  @Getter
  @Setter
  @Builder
  @EqualsAndHashCode
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderItem {
    private Integer pid;
    private String name;
    private BigDecimal price;
    private Integer amount;
  }
}
