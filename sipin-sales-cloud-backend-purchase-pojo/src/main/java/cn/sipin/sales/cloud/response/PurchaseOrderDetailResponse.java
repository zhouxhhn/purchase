/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.response;

import com.baomidou.mybatisplus.annotations.TableField;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "采购订单详情(PurchaseOrderDetailResponse)")
public class PurchaseOrderDetailResponse implements Serializable {

  private static final long serialVersionUID = -8749637728412001234L;

  /**
   * 订单详情交易流水号
   */
  private String detailNo;

  /**
   * SKU
   */
  @ApiModelProperty(value = "SKU")
  private String skuSn;

  /**
   * 产品SKU编号
   */
  @ApiModelProperty(value = "产品SKU编号")
  private String skuNo;

  /**
   * 商品名称
   */
  @ApiModelProperty(value = "商品名称")
  private String name;

  /**
   * 物料规格
   */
  @ApiModelProperty(value = "物料规格")
  private String specification;

  /**
   * 物料材质
   */
  @ApiModelProperty(value = "物料材质")
  private String texture;

  /**
   * 物料颜色
   */
  @ApiModelProperty(value = "物料颜色")
  private String color;

  /**
   * 图片地址
   */
  @ApiModelProperty(value = "图片地址")
  private String imgPath;

  /**
   * 采购数
   */
  @ApiModelProperty(value = "采购数")
  private Integer total;

  /**
   * 已入库数
   */
  @ApiModelProperty(value = "已入库数")
  private Integer stockInTotal;

  /**
   * 售价
   */
  @ApiModelProperty(value = "售价")
  private BigDecimal amount;

  /**
   * 折后价
   */
  @ApiModelProperty(value = "折后价")
  private BigDecimal discountAmount;

  /**
   * 备注
   */
  @ApiModelProperty(value = "备注")
  private String note;

  /**
   * 现采购价
   */
  @ApiModelProperty(value = "现采购价")
  private BigDecimal newDiscountAmount;

  /**
   *现采购数量
   */
  @ApiModelProperty(value = "现采购数量")
  private Integer newTotal;

  /**
   * 修改标志
   */
  @ApiModelProperty(value = "修改标志")
  private Integer flag;
}
