/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDateTime;

import javax.validation.constraints.Past;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "门店-采购订单查询请求(PurchaseOrderListRequest)")
public class PurchaseOrderListRequest {

  @ApiModelProperty(value = "采购订单号")
  private String orderNo;

  @ApiModelProperty(value = "订单开始时间")
  @DateTimeFormat(iso = ISO.DATE_TIME)
  @Past(message = "必须是过去时间")
  private LocalDateTime startTimeAt;

  @ApiModelProperty(value = "订单结束时间")
  @DateTimeFormat(iso = ISO.DATE_TIME)
  @Past(message = "必须是过去时间")
  private LocalDateTime endTimeAt;

  @ApiModelProperty(value = "门店Code")
  private String shopCode;

  @ApiModelProperty(value = "订单状态")
  private Integer statusId;

}
