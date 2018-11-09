/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "门店-采购订单修改商品(PurchaseUpdateMaterialRequest)")
public class PurchaseUpdateMaterialRequest {


  @NotNull(message = "采购数量不能为空")
  @ApiModelProperty(value = "采购数量",required = true)
  private Integer newTotal;

  @NotNull(message = "现采购价不能为空")
  @ApiModelProperty(value = "现采购价",required = true)
  private BigDecimal newDiscountAmount;

}
