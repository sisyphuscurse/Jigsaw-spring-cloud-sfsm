package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderItemJpaRepository extends CrudRepository<OrderItem, Integer> {

  List<OrderItem> findByOid(Integer oid);
}
