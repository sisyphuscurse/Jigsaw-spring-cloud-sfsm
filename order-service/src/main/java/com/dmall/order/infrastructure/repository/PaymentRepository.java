package com.dmall.order.infrastructure.repository;

import com.dmall.order.dto.PaymentDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PaymentRepository extends CrudRepository<PaymentDTO, Integer> {

  PaymentDTO findByOid(Integer oid);

}
