/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;

import java.util.Map;
import java.util.TreeMap;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ErpOrdersRequest extends ErpRequest {

  /**
   * 开始时间
   */
  @NotNull(message = "开始时间不能为空")
  private String beginDate;

  /**
   * 结束时间
   */
  @NotNull(message = "结束时间不能为空")
  private String endDate;

  /**
   * 订单状态
   */
  private Integer orderStatusId;

  /**
   * 页码
   */
  private Integer page = 1;

  /**
   * 每页数
   */
  private Integer pageSize = 50;

  /**
   * 排序类型 1 => 按订单创建时间 other => 按订单修改时间
   */
  private Integer sortType = Integer.valueOf(1);

  @Override
  public Map<String, String> toParams() {
    TreeMap<String, String> params = new TreeMap<>();
    params.put("beginDate", this.beginDate);
    params.put("endDate", this.endDate);
    params.put("orderStatusId", String.valueOf(this.orderStatusId));
    params.put("page", String.valueOf(this.page));
    params.put("pageSize", String.valueOf(this.pageSize));
    params.put("sortType", String.valueOf(sortType));

    return params;
  }
}
