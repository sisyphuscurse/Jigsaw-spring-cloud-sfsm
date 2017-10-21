package com.dmall.order.api;


import com.dmall.order.api.mapper.OrderDtoMapper;
import com.dmall.order.api.request.CreateOrderRequest;
import com.dmall.order.api.response.OrderResponse;
import com.dmall.order.application.OrderService;
import com.dmall.order.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Scope(scopeName = "")
@RequestMapping("/")
//TODO [Barry] 是否可以把API这一层去掉，直接利用OrderService + Order Service依赖的Value Classes和HTTP直接建立对应关系
//TODO [Barry] 关于HTTP与Service的绑定关系，如果能保持一致，不会有额外的变化因素导致HTTP层次与Service层次各自修改范围不同，那么我认为没必要独立分层。
public class OrderAPI {

  @Autowired
  //TODO [Barry] 一致地，如果OrderService本身能做能接口定义（或者单独一个OrderService interface)，那么这个角色也可以省略
  private OrderDtoMapper orderDtoMapper;

  @Autowired
  private OrderService orderService;

  @Autowired
  public OrderAPI(OrderService orderService) {
    this.orderService = orderService;
  }

  @RequestMapping(value = "orders", method = RequestMethod.POST, headers = "Accept=application/json")
  public OrderResponse create_new_order(CreateOrderRequest request) {
    //TODO [Barry] CreateOrderRequest是OrderService.createOrder的直接参数，不需要额外转换。另外Order JPA Entity对象不需要在Service层面暴漏出来。
    // 结合上边立论，OrderService.createOrder方法可以直接以CreateOrderRequest作为参数。Order JPA Entity在上下游传来传去是很方便，但是打破了封装，
    // 如果api层次的mapper给Order JPA Entity设置了不合适的业务属性（比如状态），那么就打破了业务规则，
    // 所以Order JPA Entity的构建应该在领域服务Order Service内部构建。至于能不能直接作为Service的Response给出去，这个看情况。
    final OrderDTO order = orderService.createOrder(orderDtoMapper.fromApi(request));
    return orderDtoMapper.toApi(order);
  }

  @RequestMapping(value = "orders/{oid}", method = RequestMethod.GET, headers = "Accept=application/json")
  public OrderResponse get_current_order(@PathVariable("oid") Integer oid) {
    //return orderDtoMapper.toApi(orderService.getOrderById(oid));

    return null;
  }


  @RequestMapping(value = "orders/{oid}/set-paid", method = RequestMethod.POST, headers = "Accept=application/json")
  public void setPaid(@PathVariable("oid") Integer oid, String payment_id, String payment_time) {
    //TODO [Barry] notifyPaid这种命名的意图存在问题。
    // A let B doSomething, doSomething是B的方法。通常notifyObservable是Observable的方法， 而不是Observer的方法
    // Observer可以用update来命名。这是Observer设计模式中的标准模型。虽然setOrderPaidOnUpdate不是个好的命名方法，
    // 但能更正确体现出"交互"关系，或者请大牛们给出更好的名字。OrderService本身不是Observable，也没有notify能力。
    // 在OO设计中这种命名问题非常普遍，特此讨论。

    orderService.notifyPaid(oid, payment_id, payment_time);
  }

  @RequestMapping(value = "/orders/{oid}/deliver", method = RequestMethod.POST, headers = "Accept=application/json")
  public void deliver(@PathVariable("oid") Integer oid, String shipping_id, String shipments_time) {
    //TODO [Barry] 同上
    orderService.notifyInDelivery(oid, shipping_id, shipments_time);
  }

  @RequestMapping(value = "/orders/{oid}/set-received", method = RequestMethod.POST, headers = "Accept=application/json")
  public void receive(@PathVariable("oid") Integer oid, Integer shipping_id, String receive_time) {
    //TODO [Barry] 同上
    orderService.notifyReceived(oid, shipping_id, receive_time);
  }

  @RequestMapping(value = "/orders/{oid}/confirm", method = RequestMethod.POST, headers = "Accept=application/json")
  //TODO [Barry] 截至目前，除了create_new_order不合适的打破了封装多了一个orderDtoMapper.fromApi(request)操作，其余所有方法
  // 都只有一行直接对OrderService裸调。基本上这个类是多余的BoilerPlate。
  public void confirm(@PathVariable("oid") Integer oid, String uid) {
    orderService.confirmOrder(oid, uid);
  }

  @RequestMapping(value = "/orders/{oid}/cancel", method = RequestMethod.POST, headers = "Accept=application/json")
  public void cancel(@PathVariable("oid") Integer oid, Integer uid, String reason) {
    orderService.cancelOrder(oid, uid, reason);
  }
}


