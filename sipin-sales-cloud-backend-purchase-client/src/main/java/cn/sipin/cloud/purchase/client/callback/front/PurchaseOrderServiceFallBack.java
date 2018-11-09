/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.callback.front;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.stereotype.Component;

import cn.sipin.cloud.purchase.client.service.front.PurchaseOrderService;
import cn.sipin.sales.cloud.request.PurchaseOrderListRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderNoteRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderRequest;
import cn.sipin.sales.cloud.request.PurchasePaymentAffirmRequest;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class PurchaseOrderServiceFallBack implements PurchaseOrderService {

  @Override public ResponseData<Page<PurchaseOrderResponse>> index(Integer page, Integer size, PurchaseOrderListRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData<PurchaseOrderResponse> selectOne(String orderNo) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData create(PurchaseOrderRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData updateNote(String orderNo, PurchaseOrderNoteRequest note) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData cancel(String orderNo) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData purchasePayment(PurchasePaymentAffirmRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData delete(String orderNo) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
