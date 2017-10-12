package com.dmall.order.domain;



//Created -> Paid : notify paid
//    Created --> Cancelled : cancel order
//    Cancelled --> [*]
//    Paid -> InDelivery : article shipped
//    InDelivery --> Received : sign for
//    Received --> Confirmed : confirm order
//    Confirmed --> [*]

public enum OrderStates {
  Created,
  Cancelled,
  Paid,
  InDelivery,
  Received,
  Confirmed
}
