/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 采购消费服务应用主入口
 */
@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.sipin.cloud.purchase.client.*","cn.siyue.platform.*"})
public class PurchaseClientApplication {
  public static void main(String[] args) {
    SpringApplication.run(PurchaseClientApplication.class);
  }
}
