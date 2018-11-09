/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import cn.sipin.cloud.purchase.client.service.backend.BackendPurchaseOrderDetailService;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "经销商_采购订单详情_后台API")
public class BackendPurchaseOrderDetailController {

  private BackendPurchaseOrderDetailService purchaseOrderDetailService;

  @Autowired
  public BackendPurchaseOrderDetailController(BackendPurchaseOrderDetailService purchaseOrderDetailService) {
    this.purchaseOrderDetailService = purchaseOrderDetailService;
  }

  @ApiOperation(nickname = "backendGetPurchaseOrderDetails", value = "后台获取采购订单详情", httpMethod = "GET")
  @GetMapping(value = "/backend/purchase-order/detail/{orderNo}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<List<PurchaseOrderDetailResponse>> index(@PathVariable(value = "orderNo") String orderNo) {

    try {
      return purchaseOrderDetailService.index(orderNo);
    } catch (Exception e) {

      return new ResponseData<List<PurchaseOrderDetailResponse>>(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }
}
