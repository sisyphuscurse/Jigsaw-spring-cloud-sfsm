package com.dmall.order.application;

import com.dmall.order.application.mapper.OrderMapper;
import com.dmall.order.domain.Order;
import com.dmall.order.domain.OrderEntity;
import com.dmall.order.domain.OrderEntityFactory;
import com.dmall.order.domain.OrderEvents;
import com.dmall.order.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
//TODO [Barry] 领域服务 vs. 应用服务。[Leo] 应用服务
public class OrderService {

  @Autowired
  private OrderEntityFactory orderEntityFactory;

  @Autowired
  private OrderMapper orderMapper;

  public OrderDTO createOrder(OrderDTO orderDTO) {
    Order order = orderMapper.fromDto(orderDTO);
    OrderEntity orderEntity = orderEntityFactory.build(order);

    return orderMapper.toDto(orderEntity.getOrder());
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
      throw new ApplicationServiceException("The user is not match, cancellation is failed.");
    }

    //TODO [Barry][SSM] 这是Spring FSM中显著的Boiler Plate，标注为了统计Boiler Plate个数。
    Message<OrderEvents> message = MessageBuilder
        .withPayload(OrderEvents.OrderCancelled)
        .setHeader("reason", reason)
        .build();

    order.sendEvent(message);
  }

}
