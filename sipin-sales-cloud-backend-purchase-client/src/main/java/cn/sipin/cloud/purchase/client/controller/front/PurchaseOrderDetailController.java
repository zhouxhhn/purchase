/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cn.sipin.cloud.purchase.client.service.front.PurchaseOrderDetailService;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "经销商_采购订单详情-前台API")
public class PurchaseOrderDetailController {

  private PurchaseOrderDetailService purchaseOrderDetailService;

  @Autowired
  public PurchaseOrderDetailController(PurchaseOrderDetailService purchaseOrderDetailService) {
    this.purchaseOrderDetailService = purchaseOrderDetailService;
  }

  @ApiOperation(nickname = "frontGetPurchaseOrderDetails", value = "前台获取采购订单详情", httpMethod = "GET")
  @GetMapping(value = "/front/purchase-order/detail/{orderNo}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData index(@PathVariable(value = "orderNo") String orderNo) {

    try {
      return purchaseOrderDetailService.index(orderNo);
    } catch (Exception e) {

      return ResponseData.build(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

//  @ApiOperation(nickname = "createPurchaseOrderDetail", value = "新增采购订单详情", httpMethod = "POST")
//  @PostMapping(value = "/purchase-order/{orderNo}/detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//  public ResponseData store(
//      @PathVariable(value = "orderNo") String orderNo,
//      @RequestBody PurchaseOrderDetailRequest request
//  ) {
//
//    try {
//      return purchaseOrderDetailService.create(orderNo, request);
//    } catch (Exception e) {
//
//      return ResponseData.build(
//          ResponseBackCode.ERROR_FAIL.getValue(),
//          ResponseBackCode.ERROR_FAIL.getMessage()
//      );
//    }
//  }
//
//  @ApiOperation(nickname = "deletePurchaseOrderDetail", value = "删除采购订单详情", httpMethod = "DELETE")
//  @PutMapping(value = "/purchase-order/{orderNo}/detail/delete/{detailId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//  public ResponseData delete(
//      @PathVariable(value = "orderNo") String orderNo,
//      @PathVariable(value = "detailId") Long detailId
//  ) {
//    try {
//      return purchaseOrderDetailService.delete(orderNo, detailId);
//    } catch (Exception e) {
//
//      return ResponseData.build(
//          ResponseBackCode.ERROR_FAIL.getValue(),
//          ResponseBackCode.ERROR_FAIL.getMessage()
//      );
//    }
//  }
}
