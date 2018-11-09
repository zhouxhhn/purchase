/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.feign.callback;


import org.springframework.stereotype.Component;

import cn.sipin.sales.cloud.purchase.service.feign.request.PurchasePaymentRequest;
import cn.sipin.sales.cloud.purchase.service.feign.service.SalesFrontService;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

/**
 * 经销商前台熔断器
 */
@Component
public class SalesFrontServiceFallBack implements SalesFrontService {



  @Override public ResponseData purchasePayment(PurchasePaymentRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData getPaymentNoByPurchaseNo(String purchaseOrderNo) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
