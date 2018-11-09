/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import cn.sipin.sales.cloud.request.vo.AddressVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "采购订单(PurchaseOrderRequest)")
public class PurchaseOrderRequest extends CrsfTokenRequest{

  @NotNull(message = "收货地址不能为空")
  private AddressVo addressVo;

  @ApiModelProperty(value = "备注")
  private String note;

  @NotNull(message = "订单详情不能为空")
  private List<PurchaseOrderDetailRequest> details = new ArrayList<>();

  @Data
  @ApiModel(value = "采购订单详情(PurchaseOrderDetailRequest)")
  public static class PurchaseOrderDetailRequest {

    @ApiModelProperty(value = "sku-sn", required = false)
    private String skuSn;

    @ApiModelProperty(value = "sku编码", required = true)
    @NotNull(message = "sku编码不能为空")
    private String skuNo;

    @ApiModelProperty(value = "采购数", required = true)
    @NotNull(message = "采购数不能为空")
    private Integer quantity;

    @ApiModelProperty(value = "备注", required = false)
    private String note;
  }

}
