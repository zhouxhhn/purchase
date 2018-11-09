/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "门店-采购订单新增商品(PurchaseAddMaterialRequest)")
public class PurchaseAddMaterialRequest {

  @NotNull(message = "采购订单号不能为空")
  @ApiModelProperty(value = "采购订单号",required = true)
  private String orderNo;

  @NotNull(message = "商品sku不能为空")
  @ApiModelProperty(value = "商品sku",required = true)
  private String skuNo;

  @NotNull(message = "商品sn不能为空")
  @ApiModelProperty(value = "商品sn",required = true)
  private String skuSn;

  @NotNull(message = "商品名称不能为空")
  @ApiModelProperty(value = "商品名称",required = true)
  private String name;

  @ApiModelProperty(value = "物料规格")
  private String specification;

  @ApiModelProperty(value = "物料材质")
  private String texture;

  @ApiModelProperty(value = "物料颜色")
  private String color;

  @ApiModelProperty(value = "图片地址")
  private String imgPath;

  @ApiModelProperty(value = "原采购数量")
  private Integer total = 0;

  @NotNull(message = "采购数量不能为空")
  @ApiModelProperty(value = "采购数量",required = true)
  private Integer newTotal;

  @NotNull(message = "现采购价不能为空")
  @ApiModelProperty(value = "现采购价",required = true)
  private BigDecimal newDiscountAmount;

  @NotNull(message = "售价不能为空")
  @ApiModelProperty(value = "售价",required = true)
  private BigDecimal amount;

  @NotNull(message = "原采购价不能为空")
  @ApiModelProperty(value = "原采购价",required = true)
  private BigDecimal discountAmount;

}
