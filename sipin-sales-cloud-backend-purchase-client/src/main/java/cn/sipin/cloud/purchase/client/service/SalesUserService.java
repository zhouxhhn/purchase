/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sipin.cloud.purchase.client.callback.SalesUserServiceFallBack;
import cn.siyue.platform.base.ResponseData;

/**
 * 调用会员管理服务生产者的接口
 */
@FeignClient(name = "sales-member-service", fallback = SalesUserServiceFallBack.class)
public interface SalesUserService {

  @RequestMapping(value = "/sales/user/indexUser", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData indexUser(
      @RequestHeader("token") String token, @RequestParam("page") int page,
      @RequestParam("size") int size, @RequestParam("shopCode") String shopCode
  );

  @RequestMapping(value = "/sales/user/searchUser", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData searchUser(@RequestHeader("token") String token, @RequestParam("code") String code);

  @RequestMapping(value = "/sales/user/getUserByToken", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getUserByToken();

  @RequestMapping(value = "/sales/user/getUserByToken", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getUserByToken(@RequestHeader("token") String token);

}
