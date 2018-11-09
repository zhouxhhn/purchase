/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IndexUserResponse {

  @ApiModelProperty(value = "id")
  private Long id;

  @ApiModelProperty(value = "姓名")
  private String name;

  @ApiModelProperty(value = "员工编号")
  private String code;

}
