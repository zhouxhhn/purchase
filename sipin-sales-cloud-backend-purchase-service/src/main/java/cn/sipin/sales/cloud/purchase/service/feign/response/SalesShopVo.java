/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.feign.response;

import com.baomidou.mybatisplus.annotations.TableField;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SalesShopVo {
  /**
   * 门店code
   */
  private String code;

  /**
   * 门店名称
   */
  @ApiModelProperty(value = "门店名称",required = true)
  private String name;

  /**
   * 门店sourceId
   */
  @ApiModelProperty(value = "门店sourceId")
  private String sourceId;

}
