/*
 * (C) Copyright 2018 Siyue Holding Group.
 */

package cn.sipin.sales.cloud.purchase.service.schedule.config;

import org.apache.commons.lang.time.DateUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

import cn.sipin.sales.cloud.purchase.service.config.PaymentCompensationProperties;
import cn.sipin.sales.cloud.purchase.service.schedule.PaymentCompensationJob;

/**
 * 定时任务配置
 */
@Configuration
public class QuartzConfig {

  private PaymentCompensationProperties compensationProperties;

  @Autowired
  public QuartzConfig(PaymentCompensationProperties compensationProperties) {
    this.compensationProperties = compensationProperties;
  }

  /**
   * 支付补偿同步任务
   *
   * @return JobDetail
   */
  @Bean
  public JobDetail paymentCompensationSyncJobDetail() {
    return JobBuilder.newJob(PaymentCompensationJob.class)
        .withIdentity(PaymentCompensationJob.class.getName())
        .storeDurably()
        .build();
  }

  /**
   * 支付补偿同步触发器
   *
   * @return Trigger
   */
  @Bean
  public Trigger paymentCompensationSyncJobTrigger() {
    SimpleScheduleBuilder scheduleBuilder =
        SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInMinutes(compensationProperties.getInterval())
            .repeatForever();

    return TriggerBuilder.newTrigger()
        .withIdentity(PaymentCompensationJob.class.getName())
        .startAt(DateUtils.addMinutes(new Date(), compensationProperties.getDelay()))
        .forJob(paymentCompensationSyncJobDetail())
        .withSchedule(scheduleBuilder)
        .build();
  }
}
