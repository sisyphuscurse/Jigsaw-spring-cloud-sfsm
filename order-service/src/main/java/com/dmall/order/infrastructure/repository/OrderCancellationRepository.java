package com.dmall.order.infrastructure.repository;


import com.dmall.order.dto.OrderCancellationDTO;
import org.springframework.data.repository.CrudRepository;

public interface OrderCancellationRepository extends CrudRepository<OrderCancellationDTO, Integer> {
  OrderCancellationDTO findByOid(Integer oid);
}
