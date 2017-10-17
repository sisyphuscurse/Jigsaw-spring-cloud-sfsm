package com.dmall.order.domain;


import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

//@Configuration
//@EnableStateMachineFactory
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {

//  @Override
//  public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception {
//    config
//        .withConfiguration()
//        .machineId("orderStateMachine");
//  }
//
//  @Override
//  public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
//    states
//        .withStates()
//        .initial(OrderStates.Created)
//        .states(EnumSet.allOf(OrderStates.class));
//  }
//
//  @Override
//  public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
//    transitions
//        .withExternal()
//        .source(OrderStates.Created).target(OrderStates.Paid)
//        .event(OrderEvents.OrderPaid)
//        .and()
//        .withExternal()
//        .source(OrderStates.Paid).target(OrderStates.InDelivery)
//        .event(OrderEvents.OrderShipped)
//        .and()
//        .withExternal()
//        .source(OrderStates.InDelivery).target(OrderStates.Received)
//        .event(OrderEvents.OrderReceived)
//        .and()
//        .withExternal()
//        .source(OrderStates.Received).target(OrderStates.Confirmed)
//        .event(OrderEvents.OrderConfirmed)
//        .and()
//        .withExternal()
//        .source(OrderStates.Created).target(OrderStates.Cancelled)
//        .event(OrderEvents.OrderCancelled);
//  }
}
