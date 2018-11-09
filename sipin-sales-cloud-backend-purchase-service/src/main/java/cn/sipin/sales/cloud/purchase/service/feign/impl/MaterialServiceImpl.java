/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.feign.impl;

import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cn.sipin.sales.cloud.purchase.service.feign.service.MaterialService;
import cn.sipin.sales.cloud.purchase.service.util.JsonTransformer;
import cn.sipin.sales.cloud.response.MaterialResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;

@Service
public class MaterialServiceImpl {

  private MaterialService materialService;

  @Autowired
  public MaterialServiceImpl(MaterialService materialService) {
    this.materialService = materialService;
  }

  public List<MaterialResponse> getSkus(String skuNos, Integer skuSize) {
    ResponseData responseData = materialService.getSkusBySkuNos(skuNos);
    if (!responseData.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      throw new RequestException(
          ResponseBackCode.FILE_NOT_FOUND.getValue(),
          "获取商品服务返回失败"
      );
    }

    List<MaterialResponse> materialResponses = JsonTransformer
        .getObjectMapper()
        .convertValue(responseData.getData(), new TypeReference<List<MaterialResponse>>() {});

    if (materialResponses.size() != skuSize) {
      throw new RequestException(
          ResponseBackCode.FILE_NOT_FOUND.getValue(),
          "无法从商品服务获取完整的商品数据"
      );
    }

    return materialResponses;
  }
}
