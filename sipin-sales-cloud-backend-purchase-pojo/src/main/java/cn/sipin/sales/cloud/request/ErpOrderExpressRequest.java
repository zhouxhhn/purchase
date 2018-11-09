/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;

import java.util.Map;
import java.util.TreeMap;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ErpOrderExpressRequest extends ErpRequest {

  @NotNull
  private String orderNo;

  @NotNull
  private String expressNo;

  @NotNull
  private String expressCompanyName;

  @NotNull
  private String companyCode;

  @Override public Map<String, String> toParams() {
    TreeMap<String, String> params = new TreeMap<>();
    params.put("orderNo", this.orderNo);
    params.put("expressNo", this.expressNo);
    params.put("expressCompanyName", this.expressCompanyName);
    params.put("companyCode", this.companyCode);

    return params;
  }
}
