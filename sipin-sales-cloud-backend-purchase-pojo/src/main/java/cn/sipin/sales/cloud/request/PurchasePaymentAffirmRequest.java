/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "PurchasePaymentAffirmRequest")
@Data
public class PurchasePaymentAffirmRequest {

  @NotBlank(message = "采购订单号不能为空")
  @ApiModelProperty(value = "purchaseOrderNo",required = true)
  private String purchaseOrderNo;
}
