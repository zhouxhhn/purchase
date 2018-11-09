/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.feign.response;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SalesAgencyIndexResponse {

  @ApiModelProperty(value = "经销商名称", required = true)
  private String name;

  @ApiModelProperty(value = "经销商帐号")
  private String code;

  /**
   * 备注
   */
  @ApiModelProperty(value = "外部编码")
  private String outerCode;

  private List<SalesShopVo> shopResponseList =new ArrayList<>(3);

}
