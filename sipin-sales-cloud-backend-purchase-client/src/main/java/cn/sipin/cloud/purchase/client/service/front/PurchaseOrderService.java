/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.service.front;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sipin.cloud.purchase.client.callback.front.PurchaseOrderServiceFallBack;
import cn.sipin.sales.cloud.request.PurchaseOrderListRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderNoteRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderRequest;
import cn.sipin.sales.cloud.request.PurchasePaymentAffirmRequest;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "purchase-service", path = "/front/purchase-order", fallback = PurchaseOrderServiceFallBack.class)
public interface PurchaseOrderService {

  @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseData<Page<PurchaseOrderResponse>> index(
      @RequestParam(value = "page") Integer page,
      @RequestParam(value = "size") Integer size,
      PurchaseOrderListRequest request
  );

  @RequestMapping(value = "/{orderNo}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<PurchaseOrderResponse> selectOne(@PathVariable(value = "orderNo") String orderNo);

  @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<PurchaseOrderResponse> create(PurchaseOrderRequest request);

  @RequestMapping(value = "/update/{orderNo}/note", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData updateNote(
      @PathVariable(value = "orderNo") String orderNo,
      PurchaseOrderNoteRequest note
  );

  @RequestMapping(value = "/cancel/{orderNo}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData cancel(@PathVariable(value = "orderNo") String orderNo);


  @RequestMapping(value = "/purchasePayment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData purchasePayment(PurchasePaymentAffirmRequest request);

  @RequestMapping(value = "/delete/{orderNo}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData delete(@PathVariable(value = "orderNo") String orderNo);
}
