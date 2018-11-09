/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.response;

import lombok.Data;

@Data
public class AgencyCodeResponse {

  private String shopName;
  private String shopCode;

  private String agencyName;
  private String agencyCode;

  private String userName;
  private String userId;

  /**
   * 折扣
   */
  private String agencyDiscount;
}
