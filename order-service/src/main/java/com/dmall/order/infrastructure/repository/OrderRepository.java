package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrderRepository extends CrudRepository<Order, Integer> {

}
