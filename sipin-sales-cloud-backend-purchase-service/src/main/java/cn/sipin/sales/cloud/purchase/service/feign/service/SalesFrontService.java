/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.feign.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.sipin.sales.cloud.purchase.service.feign.callback.SalesFrontServiceFallBack;
import cn.sipin.sales.cloud.purchase.service.feign.request.PurchasePaymentRequest;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "sales-member-service",path = "/sales/front", fallback = SalesFrontServiceFallBack.class)
public interface SalesFrontService {

  @RequestMapping(value = "/purchasePayment", method = RequestMethod.POST)
  ResponseData purchasePayment(PurchasePaymentRequest request);

  @RequestMapping(value = "/getPaymentNo/{purchaseOrderNo}", method = RequestMethod.GET)
  ResponseData getPaymentNoByPurchaseNo(@PathVariable(value = "purchaseOrderNo") String purchaseOrderNo);
}
