package com.dmall.order.infrastructure.repository;

import com.dmall.order.dto.OrderDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrderRepository extends CrudRepository<OrderDto, Integer> {

}
