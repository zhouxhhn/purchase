/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.controller.front;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import cn.sipin.cloud.purchase.client.annotation.CsrfToken;
import cn.sipin.cloud.purchase.client.service.front.PurchaseOrderService;
import cn.sipin.sales.cloud.request.PurchaseOrderListRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderNoteRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderRequest;
import cn.sipin.sales.cloud.request.PurchasePaymentAffirmRequest;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "经销商_采购订单_前台API")
@RequestMapping(path = "/front/purchase-order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PurchaseOrderController {

  private PurchaseOrderService purchaseOrderService;

  @Autowired
  public PurchaseOrderController(
      PurchaseOrderService purchaseOrderService
  ) {
    this.purchaseOrderService = purchaseOrderService;
  }

  @ApiOperation(nickname = "frontPurchaseOrderList", value = "前台获取采购订单列表", httpMethod = "GET")
  @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<PurchaseOrderResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      PurchaseOrderListRequest request
  ) {

    try {
      request.setShopCode(null);
      return purchaseOrderService.index(page, size, request);
    } catch (Exception e) {

      return new ResponseData<Page<PurchaseOrderResponse>>(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  @ApiOperation(nickname = "frontShowPurchaseOrder", value = "前台获取采购单详情")
  @GetMapping("/{orderNo}")
  public ResponseData<PurchaseOrderResponse> show(@PathVariable String orderNo) {

    try {
      return purchaseOrderService.selectOne(orderNo);
    } catch (Exception e) {

      return new ResponseData<PurchaseOrderResponse>(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  @ApiOperation(nickname = "frontCreatePurchaseOrder", value = "前台新增采购")
  @PostMapping("/create")
  @CsrfToken
  public ResponseData<PurchaseOrderResponse> store(
      @RequestBody PurchaseOrderRequest request
  ) {
    try {
      return purchaseOrderService.create(request);
    } catch (Exception e) {

      return new ResponseData<PurchaseOrderResponse>(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  @ApiOperation(nickname = "frontUpdatePurchaseOrderNote", value = "前台-更新备注")
  @PutMapping("/update/{orderNo}/note")
  public ResponseData updateNote(
      @PathVariable(value = "orderNo") String orderNo,
      @RequestBody PurchaseOrderNoteRequest note
  ) {

    return purchaseOrderService.updateNote(orderNo, note);
  }

  @ApiOperation(nickname = "frontDeletePurchaseOrder", value = "前台取消采购订单")
  @PutMapping("/cancel/{orderNo}")
  public ResponseData cancel(
      @PathVariable(value = "orderNo") String orderNo
  ) {
    try {
      return purchaseOrderService.cancel(orderNo);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseData.build(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  /**
   * 经销商采购订单支付接口
   */
  @ApiOperation(nickname = "frontPurchasePaymentAffirm", value = "前台经销商采购订单支付确认接口")
  @PostMapping(value = "/purchasePayment")
  public ResponseData purchasePayment(@RequestBody @Valid PurchasePaymentAffirmRequest request, BindingResult result) {
    if (result.hasErrors()) {
      return ResponseData.build(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          ResponseBackCode.ERROR_PARAM_INVALID.getMessage(), result.getAllErrors()
      );
    }
    return purchaseOrderService.purchasePayment(request);
  }

  @ApiOperation(nickname = "frontDeletePurchaseOrder", value = "前台删除采购订单")
  @PutMapping("/delete/{orderNo}")
  public ResponseData delete(
      @PathVariable(value = "orderNo") String orderNo
  ) {
    try {
      return purchaseOrderService.delete(orderNo);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseData.build(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }
}
