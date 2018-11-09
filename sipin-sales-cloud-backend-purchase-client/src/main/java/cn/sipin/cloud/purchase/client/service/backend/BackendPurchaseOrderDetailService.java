/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.service.backend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import cn.sipin.cloud.purchase.client.callback.backend.BackendPurchaseOrderDetailServiceFallBack;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "purchase-service" , path = "/backend/purchase-order", fallback = BackendPurchaseOrderDetailServiceFallBack.class)
public interface BackendPurchaseOrderDetailService {

  @RequestMapping(value = "/{orderNo}/detail", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<List<PurchaseOrderDetailResponse>> index(
      @PathVariable(value = "orderNo") String orderNo
  );

//  @RequestMapping(value = "/{orderNo}/detail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//  ResponseData create(
//      @PathVariable(value = "orderNo") String orderNo,
//      PurchaseOrderDetailRequest request
//  );
//
//  @RequestMapping(value = "/{orderNo}/detail/delete/{detailId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
//  ResponseData delete(
//      @PathVariable(value = "orderNo") String orderNo,
//      @PathVariable(value = "detailId") Long detailId
//  );
}
