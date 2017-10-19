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
public class OrderService implements IOrderRepository {

  @Autowired
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

  public OrderDTO createOrder(OrderDTO orderDTO) {
    return save(orderDTO);
  }

  public void notifyPaid(Integer oid, String payment_id, String payment_time) {

    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderPaid)
        .setHeader("payment_id", payment_id)
        .setHeader("payment_time", payment_time)
        .build();

    final OrderEntity order = orderEntityFactory.build(oid);
    order.sendEvent(message);
  }

  public void notifyInDelivery(Integer oid, String shipping_id, String shipping_time) {
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderShipped)
        .setHeader("shipping_id", shipping_id)
        .setHeader("shipping_time", shipping_time)
        .build();

    final OrderEntity order = orderEntityFactory.build(oid);
    order.sendEvent(message);
  }

  public void notifyReceived(Integer oid, Integer shipping_id, String received_time) {
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
      throw new RuntimeException("The user is not match, cancellation is failed.");
    }

    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderCancelled)
        .setHeader("reason", reason)
        .build();

    order.sendEvent(message);
  }

  public Order getOrderById(Integer oid) {
    OrderDTO orderDTO = repository.findOne(oid);

    if (Objects.nonNull(orderDTO)) {
      orderDTO.setPayment(paymentRepository.findByOid(oid));
      orderDTO.setShipment(shipmentRepository.findByOid(oid));
      orderDTO.setItems(orderItemRepository.findByOid(oid));
      orderDTO.setOrderCancellation(orderCancellationRepository.findByOid(oid));
    }

    return orderMapper.fromDto(orderDTO);
  }

  public Order save(Order order) {

    final OrderDTO orderDTO = orderMapper.toDto(order);

    return orderMapper.fromDto(save(orderDTO));
  }

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
