/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.feign.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "IndexAgencyRequest")
@Data
public class IndexAgencyRequest {

  @ApiModelProperty(value = "经销商名称")
  private String name;

  @ApiModelProperty(value = "经销商编码")
  private String code;

  @ApiModelProperty(value = "经销商等级")
  private String grade;
}
