/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.callback.backend;

import org.springframework.stereotype.Component;

import java.util.List;

import cn.sipin.cloud.purchase.client.service.backend.BackendPurchaseOrderDetailService;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class BackendPurchaseOrderDetailServiceFallBack implements BackendPurchaseOrderDetailService {

  @Override public ResponseData<List<PurchaseOrderDetailResponse>> index(String orderNo) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

}
