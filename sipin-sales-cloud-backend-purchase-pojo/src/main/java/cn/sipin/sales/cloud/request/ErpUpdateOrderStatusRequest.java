/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;

import java.util.Map;
import java.util.TreeMap;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ErpUpdateOrderStatusRequest extends ErpRequest {

  @NotNull
  private String orderNo;

  @NotNull
  private Integer sourceOrderStatus;

  @NotNull
  private Integer targertOrderStatus;

  @Override public Map<String, String> toParams() {
    TreeMap<String, String> params = new TreeMap<>();
    params.put("orderNo", this.orderNo);

    params.put("sourceOrderStatus", String.valueOf(this.sourceOrderStatus));

    params.put("targertOrderStatus", String.valueOf(this.targertOrderStatus));

    return params;
  }
}
