package com.dmall.order.infrastructure.repository;

import com.dmall.order.dto.ShipmentDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ShipmentRepository extends CrudRepository<ShipmentDTO, Integer> {
  ShipmentDTO findByOid(Integer oid);

}
