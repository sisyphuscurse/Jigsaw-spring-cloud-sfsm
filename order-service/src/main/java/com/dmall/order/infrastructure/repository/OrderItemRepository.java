package com.dmall.order.infrastructure.repository;

import com.dmall.order.dto.OrderItemDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderItemRepository extends CrudRepository<OrderItemDTO, Integer> {

  List<OrderItemDTO> findByOid(Integer oid);
}
