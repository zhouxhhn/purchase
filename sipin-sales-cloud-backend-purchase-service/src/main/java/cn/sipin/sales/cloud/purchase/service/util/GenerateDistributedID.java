/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.util;

import org.apache.commons.lang.RandomStringUtils;

public class GenerateDistributedID {

  private GenerateDistributedID() {
  }

  private static final String SEED = "seed";

  /**
   * 返回位数为7的分布式ID
   */
  private static String getNextId() {
    String nextId = String.valueOf(SnowFlake.getInstance().nextId());
    int length = nextId.length();
    return nextId.substring(length - 7, length);
  }

  public static String getPurchaseOrderNo(String agentCode, String shopCode) {

    int middle = Math.abs((agentCode + shopCode).hashCode() % 99);

    String prefix = RandomStringUtils.randomNumeric(2);
    while (prefix.startsWith("0")) {
      prefix = RandomStringUtils.randomNumeric(2);
    }

    // 返回12位数字
    return "8" + prefix + String.valueOf(middle) + getNextId();
  }
}
