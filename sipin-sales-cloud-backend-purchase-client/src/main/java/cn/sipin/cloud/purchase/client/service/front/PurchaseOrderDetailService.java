/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.service.front;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.sipin.cloud.purchase.client.callback.front.PurchaseOrderDetailServiceFallBack;
import cn.sipin.sales.cloud.request.PurchaseOrderRequest.PurchaseOrderDetailRequest;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "purchase-service" , path = "/front/purchase-order", fallback = PurchaseOrderDetailServiceFallBack.class)
public interface PurchaseOrderDetailService {

  @RequestMapping(value = "/{orderNo}/detail", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData index(
      @PathVariable(value = "orderNo") String orderNo
  );

//  @RequestMapping(value = "/{orderNo}/detail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//  ResponseData create(
//      @PathVariable(value = "orderNo") String orderNo,
//      PurchaseOrderDetailRequest request);
//
//  @RequestMapping(value = "/{orderNo}/detail/delete/{detailId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
//  ResponseData delete(@PathVariable(value = "orderNo") String orderNo,
//                      @PathVariable(value = "detailId") Long detailId);
}
