# [order service]

Organize order process.

## Commands handled


### External (user)

* **createNewOrder** - user posts order info, emits **OrderCreated**, emits **OrderCreated**.
* **notifyPaid** - alipay callback notify paid api, if the user has been paid on alipay, emits **OrderPaid**.
* **confirmOrder** - user confirm order after the article has received. emits **OrderConfirmed**.
* **cancelOrder** - user post cancel request, if the order has not paid, emits **OrderCancelled**.

### Internal

* **cancelOrder** - Triggered by scheduled task when order is timeout, emits **OrderCancelled**

## Queries handled

* **getOrder** - Gets order detail.

## Events emitted

* **OrderCreated** - When a order is placed, in response to **OrderCreated**.
* **OrderPaid** - When a payment has finished, in response to **OrderPaid**.
* **OrderConfirmed** - When order has confirmed, in response to **OrderConfirmed**.
* **OrderCancelled** - When order has cancelled, in response to **OrderCancelled**.

Event emitted publicly are published via a broker topic named `order-OrderEvent`.

## Events consumed

* **ShippingService.ArticleShipped** - Update order state.
* **ShippingService.ArticleReceived** - Update order state.
