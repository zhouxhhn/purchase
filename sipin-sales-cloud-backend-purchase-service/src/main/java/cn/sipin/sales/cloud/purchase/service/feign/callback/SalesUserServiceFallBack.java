/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.feign.callback;


import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.stereotype.Component;

import cn.sipin.sales.cloud.purchase.service.feign.request.IndexAgencyRequest;
import cn.sipin.sales.cloud.purchase.service.feign.response.SalesAgencyIndexResponse;
import cn.sipin.sales.cloud.purchase.service.feign.service.SalesUserService;
import cn.sipin.sales.cloud.response.IndexSalesAgencyResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

/**
 * 门店管理员熔断器
 */
@Component
public class SalesUserServiceFallBack implements SalesUserService {

  @Override public ResponseData<Page<IndexSalesAgencyResponse>> getAllAgency() {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData<Page<SalesAgencyIndexResponse>> indexAgency(
      int page, int size, IndexAgencyRequest indexAgencyRequest
  ) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData indexUser(String token, int page, int size, String shopCode) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData searchUser(String token, String code) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }


  @Override public ResponseData getUserByToken() {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData getUserByToken(String token) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData getUserInfoList() {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
