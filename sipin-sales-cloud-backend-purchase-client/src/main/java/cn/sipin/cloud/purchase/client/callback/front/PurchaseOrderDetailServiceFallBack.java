/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.callback.front;

import org.springframework.stereotype.Component;

import cn.sipin.cloud.purchase.client.service.front.PurchaseOrderDetailService;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class PurchaseOrderDetailServiceFallBack implements PurchaseOrderDetailService {

  @Override public ResponseData index(String orderNo) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

}
