package com.dmall.order.infrastructure.repository;

import com.dmall.order.dto.PaymentDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PaymentRepository extends CrudRepository<PaymentDto, Integer> {

  PaymentDto findByOid(Integer oid);

}
