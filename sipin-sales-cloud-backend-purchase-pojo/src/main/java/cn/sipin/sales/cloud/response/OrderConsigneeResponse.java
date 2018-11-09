/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.response;

import cn.sipin.sales.cloud.response.vo.OrderConsigneeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "配送信息(OrderConsigneeResponse)")
public class OrderConsigneeResponse extends OrderConsigneeVo {

  @ApiModelProperty(value = "订单号")
  private String orderNo;

}
