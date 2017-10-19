package com.dmall.order.infrastructure.repository;

import com.dmall.order.dto.ShipmentDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ShipmentRepository extends CrudRepository<ShipmentDto, Integer> {
  ShipmentDto findByOid(Integer oid);

}
