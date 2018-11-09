/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.feign.callback;

import org.springframework.stereotype.Component;

import cn.sipin.sales.cloud.purchase.service.feign.service.MaterialService;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class MaterialServiceFallBack implements MaterialService {

  @Override public ResponseData index(Integer page, Integer size, String search) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage());
  }

  @Override public ResponseData indexWithoutStock(Integer page, Integer size, String search) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage());
  }

  @Override public ResponseData getSkusBySkuNos(String skuNos) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage());
  }

  @Override public ResponseData getSkusBySkuNos(String skuNos, String token) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage());
  }

  @Override public ResponseData getskuBygroupNumber(String skuNumber) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage());
  }

}
