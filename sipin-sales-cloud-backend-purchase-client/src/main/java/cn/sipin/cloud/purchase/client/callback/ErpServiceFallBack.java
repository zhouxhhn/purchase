/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.callback;

import org.springframework.stereotype.Component;

import cn.sipin.cloud.purchase.client.service.ErpService;
import cn.sipin.sales.cloud.request.ErpOrdersRequest;
import cn.sipin.sales.cloud.request.ErpOrderExpressRequest;
import cn.sipin.sales.cloud.request.ErpUpdateOrderStatusRequest;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class ErpServiceFallBack implements ErpService {

  @Override public ResponseData index(String sign, ErpOrdersRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData updateOrderStatus(String orderNo, ErpUpdateOrderStatusRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData createExpress(String sign, ErpOrderExpressRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

}
