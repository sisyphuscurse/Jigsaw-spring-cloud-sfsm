package com.dmall.order.infrastructure.repository;

import com.dmall.order.dto.OrderItemDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderItemRepository extends CrudRepository<OrderItemDto, Integer> {

  List<OrderItemDto> findByOid(Integer oid);
}
