/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import cn.sipin.cloud.purchase.client.service.ErpService;
import cn.sipin.sales.cloud.request.ErpOrdersRequest;
import cn.sipin.sales.cloud.request.ErpOrderExpressRequest;
import cn.sipin.sales.cloud.request.ErpUpdateOrderStatusRequest;
import cn.siyue.platform.base.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "ERP获取订单接口")
@RequestMapping(path = "/erp/purchase-orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ErpController {

  private ErpService erpService;

  @Autowired
  public ErpController(ErpService erpService) {
    this.erpService = erpService;
  }

  @PostMapping(value = "")
  @ApiOperation(nickname = "getPurchaseOrdersForErp", value = "ERP获取门店采购单列表")
  public ResponseData index(
      @RequestParam(value = "sign") String sign,
      @RequestBody ErpOrdersRequest request
  ) {
    return erpService.index(sign, request);
  }

  @PostMapping("/status/update")
  @ApiOperation(nickname = "updatePurchaseOrderStatusForErp", value = "ERP更新订单状态")
  public ResponseData updateOrderStatus(
      @RequestParam(value = "sign") String sign,
      @RequestBody @Valid ErpUpdateOrderStatusRequest request
  ) {

    return erpService.updateOrderStatus(sign, request);
  }

  @PostMapping("/express/create")
  @ApiOperation(nickname = "createExpressForErp", value = "ERP推送物流信息")
  public ResponseData createExpress(
      @RequestParam(value = "sign") String sign,
      @RequestBody @Valid ErpOrderExpressRequest request
  ) {
    return erpService.createExpress(sign, request);
  }
}
