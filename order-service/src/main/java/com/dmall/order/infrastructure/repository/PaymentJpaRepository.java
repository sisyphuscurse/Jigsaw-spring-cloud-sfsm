package com.dmall.order.infrastructure.repository;

import com.dmall.order.domain.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PaymentJpaRepository extends CrudRepository<Payment, Integer> {

  Payment findByOid(Integer oid);

}
