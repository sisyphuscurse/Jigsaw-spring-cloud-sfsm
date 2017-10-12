package com.dmall.order.infrastructure.broker;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Component
public class HystrixClientFallback implements ProductBroker {

    @Override
    public Product getProductDetial(@PathVariable("productId") String productId) {
        return new Product("default", "default", new Date());
    }
}
