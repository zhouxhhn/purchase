/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.response.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "OrderConsigneeVo")
public class OrderConsigneeVo {
  @ApiModelProperty(value = "收货人名称")
  @JsonProperty("name")
  private String name;
  /**
   * 收货人手机
   */
  @ApiModelProperty(value = "收货人手机")
  @JsonProperty("mobile")
  private String mobile;

  /**
   * 省
   */
  @ApiModelProperty(value = "省")
  @JsonProperty("province")
  private String province;
  /**
   * 市
   */
  @ApiModelProperty(value = "市")
  @JsonProperty("city")
  private String city;
  /**
   * 区
   */
  @ApiModelProperty(value = "区")
  @JsonProperty("district")
  private String district;
  /**
   * 详细地址
   */
  @ApiModelProperty(value = "详细地址")
  @JsonProperty("addr")
  private String addr;
}
