/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.controller.front;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sipin.cloud.purchase.client.aspect.CsrfTokenAspect;
import cn.sipin.cloud.purchase.client.service.RedisClusterService;
import cn.sipin.cloud.purchase.client.service.SalesUserService;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "使用token换csrfToken")
@RequestMapping(path = "/front/get-csrf-token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CsrfTokenController {

  private RedisClusterService redisClusterService;
  private SalesUserService salesUserService;

  @Autowired
  public CsrfTokenController(RedisClusterService redisClusterService, SalesUserService salesUserService) {
    this.redisClusterService = redisClusterService;
    this.salesUserService = salesUserService;
  }


  @ApiOperation(nickname = "frontGetToken", value = "前台获取CsrkToken", httpMethod = "GET")
  @GetMapping("")
  public ResponseData getToken() {

    ResponseData agencyData = salesUserService.getUserByToken();
    if (!agencyData.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      return ResponseData.build(
          ResponseBackCode.FILE_NOT_FOUND.getValue(),
          "token不存在"
      );
    }


    String crsfToKenKey = CsrfTokenAspect.PREFIX_CSRF_TOKEN + RandomStringUtils.randomAlphanumeric(22);

    // 10分钟过期
    redisClusterService.set(crsfToKenKey, "",  10);

    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        crsfToKenKey
    );
  }
}
