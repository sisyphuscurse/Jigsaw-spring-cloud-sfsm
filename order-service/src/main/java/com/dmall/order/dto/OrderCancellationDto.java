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

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_cancellation")
public class OrderCancellationDto {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private Integer oid;
  private String created_time;
  private String reason;
}
