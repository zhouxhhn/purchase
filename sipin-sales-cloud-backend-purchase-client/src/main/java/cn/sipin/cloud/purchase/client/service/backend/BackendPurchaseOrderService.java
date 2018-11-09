/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.service.backend;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sipin.cloud.purchase.client.callback.backend.BackendPurchaseOrderServiceFallBack;
import cn.sipin.cloud.purchase.client.callback.front.PurchaseOrderServiceFallBack;
import cn.sipin.sales.cloud.request.PurchaseAddMaterialRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderListRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderNoteRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderRequest;
import cn.sipin.sales.cloud.request.PurchasePaymentAffirmRequest;
import cn.sipin.sales.cloud.request.PurchaseUpdateMaterialRequest;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "purchase-service", path = "/backend/purchase-order", fallback = BackendPurchaseOrderServiceFallBack.class)
public interface BackendPurchaseOrderService {

  @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseData<Page<PurchaseOrderResponse>> index(
      @RequestParam(value = "page") Integer page,
      @RequestParam(value = "size") Integer size,
      PurchaseOrderListRequest request
  );

  @RequestMapping(value = "/{orderNo}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<PurchaseOrderResponse> selectOne(@PathVariable(value = "orderNo") String orderNo);

  @RequestMapping(value = "/update/{orderNo}/note", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData updateNote(
      @PathVariable(value = "orderNo") String orderNo,
      PurchaseOrderNoteRequest note
  );

  @RequestMapping(value = "/materialDetail/{orderNo}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<Page<PurchaseOrderDetailResponse>> materialDetail(@PathVariable(value = "orderNo") String orderNo);

  @RequestMapping(value = "/addMaterial", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData addMaterial( PurchaseAddMaterialRequest request);

  @RequestMapping(value = "/deleteMaterial/{orderNo}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData deleteMaterial(@PathVariable(value = "orderNo") String orderNo);

  @RequestMapping(value = "/updateMaterial/{orderNo}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData updateMaterial( @PathVariable(value = "orderNo") String orderNo,PurchaseUpdateMaterialRequest request);

  @RequestMapping(value = "/exportExcel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseData exportExcel(PurchaseOrderListRequest request);

}
