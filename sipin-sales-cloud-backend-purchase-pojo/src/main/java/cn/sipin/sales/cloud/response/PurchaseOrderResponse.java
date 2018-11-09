/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import cn.sipin.sales.cloud.response.vo.OrderConsigneeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "采购订单(PurchaseOrderResponse)")
public class PurchaseOrderResponse {

  /**
   * 采购单号
   */
  @ApiModelProperty(value = "采购单号")
  private String no;

  /**
   * 状态ID
   */
  @ApiModelProperty(value = "状态ID")
  private Integer statusId;

  /**
   * 订单金额
   */
  @ApiModelProperty(value = "订单金额")
  private BigDecimal amount;

  /**
   * 应付金额
   */
  @ApiModelProperty(value = "应付金额")
  private BigDecimal payableAmount;

  /**
   * 已付金额
   */
  @ApiModelProperty(value = "已付金额")
  private BigDecimal paidAmount;

  /**
   * 外部编码 K3客户编码
   */
  @ApiModelProperty(value = "外部编码")
  private String outerCode;

  /**
   * sourceId 对应斯品商城 create source Id
   */
  @ApiModelProperty(value = "sourceId")
  private String sourceId;

  /**
   * 经销商Code
   */
  @ApiModelProperty(value = "经销商Code")
  private String agencyCode;

  /**
   * 经销商名称
   */
  @ApiModelProperty(value = "经销商名称")
  private String agencyName;

  /**
   * 店铺Code
   */
  @ApiModelProperty(value = "店铺Code")
  private String shopCode;

  /**
   * 店铺名称
   */
  @ApiModelProperty(value = "店铺名称")
  private String shopName;

  /**
   * 经销商折扣
   */
  @ApiModelProperty(value = "折扣信息")
  private BigDecimal discount;

  /**
   * 创建者名称
   */
  @ApiModelProperty(value = "创建者名称")
  private String creatorName;

  /**
   * 审核者名称
   */
  @ApiModelProperty(value = "审核者名称")
  private String auditorName;

  /**
   * 审核时间
   */
  @ApiModelProperty(value = "审核时间")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime auditedAt;

  /**
   * 备注
   */
  @ApiModelProperty(value = "备注")
  private String note;

  /**
   * 创建时间
   */
  @ApiModelProperty(value = "创建时间")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime createdAt;

  /**
   * 采购订单详情
   */
  @ApiModelProperty(value = "采购订单详情")
  private List<PurchaseOrderDetailResponse> detailVos;

  @ApiModelProperty(value = "配送信息")
  private OrderConsigneeVo orderConsignee;

  @ApiModelProperty(value = "物流信息列表")
  private List<PurchaseOrderExpressResponse> orderExpressList;

}
