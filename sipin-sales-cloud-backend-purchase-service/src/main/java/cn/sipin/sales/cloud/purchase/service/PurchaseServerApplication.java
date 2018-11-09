/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 采购生产服务类入口
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.sipin.sales.cloud.purchase.service.*","cn.siyue.platform.*"})
@EnableFeignClients
@MapperScan("cn.sipin.sales.cloud.purchase.service.mapper*")
public class PurchaseServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(PurchaseServerApplication.class);

  }
}
