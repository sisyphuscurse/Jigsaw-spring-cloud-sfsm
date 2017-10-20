package com.dmall.order.application;

import com.dmall.order.application.mapper.OrderMapper;
import com.dmall.order.domain.IOrderRepository;
import com.dmall.order.domain.OrderEntity;
import com.dmall.order.domain.OrderEntityFactory;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.domain.Order;
import com.dmall.order.dto.OrderDTO;
import com.dmall.order.infrastructure.repository.OrderCancellationRepository;
import com.dmall.order.infrastructure.repository.OrderItemRepository;
import com.dmall.order.infrastructure.repository.OrderRepository;
import com.dmall.order.infrastructure.repository.PaymentRepository;
import com.dmall.order.infrastructure.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
//TODO [Barry] 领域服务 vs. 应用服务。
public class OrderService implements IOrderRepository { //TODO [Barry] Order Service竟然实现Repository...

  @Autowired
  //TODO [Barry] 如果将findById(load/get)逻辑与JPA Entity save逻辑隔离出去，
  // 那么这个层次不需要对Repository的依赖。如果有可以再封装一个概念出来。
  // 所有JPA Entity存储的逻辑不应该在这个层次出现，所有加载JPA Entity的操作也不应该在这个层次出现。
  private OrderRepository repository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private ShipmentRepository shipmentRepository;

  @Autowired
  private OrderEntityFactory orderEntityFactory;

  @Autowired
  private OrderCancellationRepository orderCancellationRepository;

  @Autowired
  private OrderMapper orderMapper;

  //TODO [Barry] 领域对象的创建，应该分两个层次，一个是service 层次触发，一个是领域层根据领域逻辑初始化业务属性。
  // 不应该由Service直接封装JPA Entity并且直接调用repository存起来。应该由Service创建一个领域对象，领域对象根据业务规则初始化JPA Entity,
  // 调用领域对象的写入方法保存状态。
  public OrderDTO createOrder(OrderDTO orderDTO) {
    return save(orderDTO);
  }
  
  public void notifyPaid(Integer oid, String payment_id, String payment_time) {

    //TODO [Barry][SSM] 这是Spring FSM中显著的Boiler Plate，没有任何注释我十分不理解。
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderPaid)
        .setHeader("payment_id", payment_id)
        .setHeader("payment_time", payment_time)
        .build();

    final OrderEntity order = orderEntityFactory.build(oid);
    order.sendEvent(message);
  }

  public void notifyInDelivery(Integer oid, String shipping_id, String shipping_time) {
    //TODO [Barry][SSM] 这是Spring FSM中显著的Boiler Plate，标注为了统计Boiler Plate个数。
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderShipped)
        .setHeader("shipping_id", shipping_id)
        .setHeader("shipping_time", shipping_time)
        .build();

    final OrderEntity order = orderEntityFactory.build(oid);
    order.sendEvent(message);
  }

  public void notifyReceived(Integer oid, Integer shipping_id, String received_time) {
    //TODO [Barry][SSM] 这是Spring FSM中显著的Boiler Plate，标注为了统计Boiler Plate个数。
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderReceived)
        .setHeader("shipping_id", shipping_id)
        .setHeader("received_time", received_time)
        .build();

    final OrderEntity order = orderEntityFactory.build(oid);
    order.sendEvent(message);
  }

  public void confirmOrder(Integer oid, String uid) {

    final OrderEntity order = orderEntityFactory.build(oid);
    order.sendEvent(OrderEvents.OrderConfirmed);
  }


  public void cancelOrder(Integer oid, Integer uid, String reason) {

    final OrderEntity order = orderEntityFactory.build(oid);


    if (!Objects.equals(uid, order.getOrder().getUid())) {
      //TODO [Barry] 应该单独定一个ServiceException，与对应的ExceptionHandler用来封装对外的错误
      throw new RuntimeException("The user is not match, cancellation is failed.");
    }

    //TODO [Barry][SSM] 这是Spring FSM中显著的Boiler Plate，标注为了统计Boiler Plate个数。
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderCancelled)
        .setHeader("reason", reason)
        .build();

    order.sendEvent(message);
  }

  public Order getOrderById(Integer oid) {
    //TODO [Barry] JPA Entity加载逻辑也应该封装在领域对象内部，包括Eager/Lazy的子领域对象的加载。
    // API导出视图可以通过工具简单实现。这个情况不绝对，JEE专家组也有建议通过单独的《值对象组装器》来完成跨"领域"组装的情况。
    // 但是对于微服务而言，为了保证业务逻辑没有被打破，还需要对领域对象有一定程度的封装，包括一些Lazy/Eager加载的非功能需求。
    OrderDTO orderDTO = repository.findOne(oid);

    if (Objects.nonNull(orderDTO)) {
      orderDTO.setPayment(paymentRepository.findByOid(oid));
      orderDTO.setShipment(shipmentRepository.findByOid(oid));
      orderDTO.setItems(orderItemRepository.findByOid(oid));
      orderDTO.setOrderCancellation(orderCancellationRepository.findByOid(oid));
    }

    return orderMapper.fromDto(orderDTO);
  }

  //TODO [Barry] 领域对象状态持久化，应该封装在领域对象内部，而不应该直接通过Service来操作，这样的话封装何在？FSM还有何意义？
  private OrderDTO save(OrderDTO orderDTO) {

    OrderDTO savedOrder = repository.save(orderDTO);

    if (Objects.nonNull(orderDTO.getItems())) {
      orderDTO.getItems().stream()
          .forEach(c -> c.setOid(savedOrder.getOid()));

      orderItemRepository.save(orderDTO.getItems());
    }

    if (Objects.nonNull(orderDTO.getPayment())) {
      paymentRepository.save(orderDTO.getPayment());
    }

    if (Objects.nonNull(orderDTO.getShipment())) {
      shipmentRepository.save(orderDTO.getShipment());
    }

    if (Objects.nonNull(orderDTO.getOrderCancellation())) {
      orderCancellationRepository.save(orderDTO.getOrderCancellation());
    }

    return orderDTO;
  }
}
