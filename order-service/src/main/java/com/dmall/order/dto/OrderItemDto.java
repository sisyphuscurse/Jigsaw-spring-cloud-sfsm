package com.dmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_items")
@Builder
public class OrderItemDto {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private Integer pid;
  private Integer oid;
  private String name;
  private BigDecimal price;
  private Integer amount;
}
