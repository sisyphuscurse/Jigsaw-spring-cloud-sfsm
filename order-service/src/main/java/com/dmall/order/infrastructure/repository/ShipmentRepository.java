package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.Shipment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ShipmentRepository extends CrudRepository<Shipment, Integer> {
  Shipment findByOid(Integer oid);

}
