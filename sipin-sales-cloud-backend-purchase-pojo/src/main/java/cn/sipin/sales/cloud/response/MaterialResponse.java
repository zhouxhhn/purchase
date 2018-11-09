/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MaterialResponse {

  /**
   * 物料编码
   */
  private String number;

  /**
   * 物料名称
   */
  private String name;

  /**
   * 物料规格
   */
  private String specification;

  /**
   * 物料材质
   */
  private String texture;

  /**
   * 物料颜色
   */
  private String color;

  /**
   * SPU
   */
  private String spu;

  /**
   * SKU
   */
  private String sku;

  /**
   * 是否启用
   */
  private Integer status;

  /**
   * 禁用状态：1为未禁用，0为已禁用
   */
  private Integer forbidStatus;

  /**
   * 图片资源
   */
  private String imgPath;

  /**
   * 售价
   */
  private BigDecimal amount;

  /**
   * 库存数
   */
  private Integer stockQty;

}
