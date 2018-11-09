/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.feign.service;


import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sipin.sales.cloud.purchase.service.feign.callback.SalesUserServiceFallBack;
import cn.sipin.sales.cloud.purchase.service.feign.request.IndexAgencyRequest;
import cn.sipin.sales.cloud.response.IndexSalesAgencyResponse;
import cn.sipin.sales.cloud.purchase.service.feign.response.SalesAgencyIndexResponse;
import cn.siyue.platform.base.ResponseData;

/**
 * 调用会员管理服务生产者的接口
 */
@FeignClient(name = "sales-member-service", fallback = SalesUserServiceFallBack.class)
public interface SalesUserService {

  @RequestMapping(value = "/sales/agency/index", method = RequestMethod.GET)
  ResponseData<Page<IndexSalesAgencyResponse>> getAllAgency();

  @RequestMapping(value = "/sales/agency/indexAgency", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseData<Page<SalesAgencyIndexResponse>> indexAgency(@RequestParam("page") int page, @RequestParam("size") int size, IndexAgencyRequest indexAgencyRequest);

  @RequestMapping(value = "/sales/user/indexUser", method = RequestMethod.GET)
  ResponseData indexUser(
      @RequestHeader("token") String token, @RequestParam("page") int page,
      @RequestParam("size") int size, @RequestParam("shopCode") String shopCode
  );

  @RequestMapping(value = "/sales/user/searchUser", method = RequestMethod.GET)
  ResponseData searchUser(@RequestHeader("token") String token, @RequestParam("code") String code);

  @RequestMapping(value = "/sales/user/getUserByToken", method = RequestMethod.GET)
  ResponseData getUserByToken();

  @RequestMapping(value = "/sales/user/getUserByToken", method = RequestMethod.GET)
  ResponseData getUserByToken(@RequestHeader("token") String token);

  @RequestMapping(value = "/sales/user/index", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getUserInfoList();


}
