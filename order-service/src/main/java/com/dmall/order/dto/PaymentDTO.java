package com.dmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Entity
@Table(name = "payments")
public class PaymentDTO {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String payment_id;

  private Integer oid;
  private String payment_time;
}
