/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.feign.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "PurchasePaymentRequest")
@Data
public class PurchasePaymentRequest {

  @NotNull(message = "采购订单号不能为空")
  @ApiModelProperty(value = "purchaseOrderNo",required = true)
  private String purchaseOrderNo;

  @NotNull(message = "金额不能为空")
  @ApiModelProperty(value = "金额",required = true)
  private BigDecimal amount;
}
