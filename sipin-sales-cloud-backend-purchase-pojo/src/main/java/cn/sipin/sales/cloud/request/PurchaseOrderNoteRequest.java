/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "门店-采购订单更新备注请求(PurchaseOrderNoteRequest)")
public class PurchaseOrderNoteRequest {

  private String note;
}
