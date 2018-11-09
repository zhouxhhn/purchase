/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 支付成功但没更新状态补偿机制配置
 */
@Component
@Data
@ConfigurationProperties(prefix = "payment.compensation")
public class PaymentCompensationProperties {

  private Integer interval;

  private Integer delay;
}
