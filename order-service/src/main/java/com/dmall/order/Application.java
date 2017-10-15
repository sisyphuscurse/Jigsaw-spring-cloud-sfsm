package com.dmall.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAutoConfiguration
@ComponentScan
@EnableCircuitBreaker
@EnableFeignClients
@EnableSwagger2
@EnableJpaRepositories(basePackages = "com.dmall.order.infrastructure.repository")
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }
}
