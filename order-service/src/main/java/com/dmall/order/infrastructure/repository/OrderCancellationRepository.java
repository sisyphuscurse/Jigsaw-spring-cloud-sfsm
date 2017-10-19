package com.dmall.order.infrastructure.repository;


import com.dmall.order.dto.OrderCancellationDto;
import org.springframework.data.repository.CrudRepository;

public interface OrderCancellationRepository extends CrudRepository<OrderCancellationDto, Integer> {
  OrderCancellationDto findByOid(Integer oid);
}
