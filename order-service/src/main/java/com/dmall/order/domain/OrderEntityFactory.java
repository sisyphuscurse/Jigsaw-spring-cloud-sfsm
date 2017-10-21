package com.dmall.order.domain;


import com.dmall.order.infrastructure.repository.OrderCancellationRepository;
import com.dmall.order.infrastructure.repository.OrderItemRepository;
import com.dmall.order.infrastructure.repository.OrderRepository;
import com.dmall.order.infrastructure.repository.PaymentRepository;
import com.dmall.order.infrastructure.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//TODO [Barry][SSM] 这也是因为Spring FSM带来的Boiler Plate，当中没有任何领域逻辑，没有任何状态机本身的信息
//TODO [Barry] EntityFactory本身是一个抽象概念，未必是一个独立的类。聚合根可能是其他聚合的EntityFactory
public class OrderEntityFactory {

  @Autowired
  private OrderStateMachineFactory orderStateMachineFactory;

  @Autowired
  private OrderRepository repository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private ShipmentRepository shipmentRepository;

  @Autowired
  private OrderCancellationRepository orderCancellationRepository;

  public OrderEntity build(Integer oid) {
    return new OrderEntity(oid, orderStateMachineFactory, repository, orderItemRepository, paymentRepository,
        shipmentRepository, orderCancellationRepository);
  }

  public OrderEntity build(Order order) {
    return new OrderEntity(order, orderStateMachineFactory, repository, orderItemRepository, paymentRepository,
        shipmentRepository, orderCancellationRepository);
  }
}
