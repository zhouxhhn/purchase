/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.controller.backend;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.type.TypeReference;

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

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.sipin.cloud.purchase.client.service.backend.BackendPurchaseOrderService;
import cn.sipin.cloud.purchase.client.utils.ExportPurchaseOrderExcelUtils;
import cn.sipin.cloud.purchase.client.utils.JsonTransformer;
import cn.sipin.sales.cloud.request.PurchaseAddMaterialRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderListRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderNoteRequest;
import cn.sipin.sales.cloud.request.PurchaseUpdateMaterialRequest;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "经销商_采购订单_后台API")
@RequestMapping(path = "/backend/purchase-order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BackendPurchaseOrderController {

  private BackendPurchaseOrderService purchaseOrderService;

  @Autowired
  public BackendPurchaseOrderController(BackendPurchaseOrderService purchaseOrderService) {
    this.purchaseOrderService = purchaseOrderService;
  }

  @ApiOperation(nickname = "backendPurchaseOrderList", value = "后台获取采购订单列表", httpMethod = "GET")
  @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<PurchaseOrderResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      PurchaseOrderListRequest request
  ) {

    try {

      return purchaseOrderService.index(page, size, request);
    } catch (Exception e) {

      return new ResponseData<Page<PurchaseOrderResponse>>(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  @ApiOperation(nickname = "backendShowPurchaseOrder", value = "后台获取采购单详情")
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

  @ApiOperation(nickname = "backendUpdatePurchaseOrderNote", value = "后台更新备注")
  @PutMapping("/update/{orderNo}/note")
  public ResponseData updateNote(
      @PathVariable(value = "orderNo") String orderNo,
      @RequestBody PurchaseOrderNoteRequest note
  ) {

    return purchaseOrderService.updateNote(orderNo, note);
  }

  @ApiOperation(nickname = "backendShowPurchaseOrderMaterialDetail", value = "后台通过采购订单获取商品列表")
  @GetMapping(value = "/materialDetail/{orderNo}")
  public ResponseData<Page<PurchaseOrderDetailResponse>> materialDetail(@PathVariable String orderNo) {
    try {
      return purchaseOrderService.materialDetail(orderNo);
    } catch (Exception e) {
      return new ResponseData<>(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  @ApiOperation(nickname = "backendPurchaseOrderAddMaterial", value = "后台新增采购订单中的商品")
  @PostMapping(value = "/addMaterial")
  public ResponseData addMaterial(@RequestBody PurchaseAddMaterialRequest request, BindingResult result) {

    //请求的数据参数格式不正确
    if (result.hasErrors()) {
      return ResponseData.build(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          ResponseBackCode.ERROR_PARAM_INVALID.getMessage(),result.getAllErrors()
      );
    }
    return purchaseOrderService.addMaterial(request);
  }

  @ApiOperation(nickname = "backendPurchaseOrderDeleteMaterial", value = "后台删除采购订单中的商品")
  @PutMapping(value = "/deleteMaterial/{orderNo}")
  public ResponseData deleteMaterial(@PathVariable String orderNo) {
    return purchaseOrderService.deleteMaterial(orderNo);
  }

  @ApiOperation(nickname = "backendPurchaseOrderUpdateMaterial", value = "后台修改采购订单中的商品")
  @PutMapping(value = "/updateMaterial/{orderNo}")
  public ResponseData updateMaterial(@PathVariable String orderNo,@RequestBody PurchaseUpdateMaterialRequest request, BindingResult result) {
    //请求的数据参数格式不正确
    if (result.hasErrors()) {
      return ResponseData.build(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          ResponseBackCode.ERROR_PARAM_INVALID.getMessage(),result.getAllErrors()
      );
    }
    return purchaseOrderService.updateMaterial(orderNo,request);
  }

  @ApiOperation(nickname = "backendPurchaseOrderExportExcel", value = "后台导出采购订单数据")
  @GetMapping(value = "/exportExcel")
  public void exportExcel(PurchaseOrderListRequest request,HttpServletResponse res) {

    try {
      ResponseData responseData = purchaseOrderService.exportExcel(request);
      List<PurchaseOrderResponse> purchaseOrderResponseList = JsonTransformer
          .getObjectMapper()
          .convertValue(responseData.getData(), new TypeReference<List<PurchaseOrderResponse>>() {});
      ExportPurchaseOrderExcelUtils.export(res, purchaseOrderResponseList);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
