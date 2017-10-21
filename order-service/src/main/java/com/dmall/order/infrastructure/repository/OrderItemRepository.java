package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderItemRepository extends CrudRepository<OrderItem, Integer> {

  List<OrderItem> findByOid(Integer oid);
}
