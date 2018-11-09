/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;


import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CrsfTokenRequest {

  @NotNull(message = "crsfToken不能为空")
  private String crsfToken;
}
