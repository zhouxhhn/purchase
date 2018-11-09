/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

import cn.sipin.cloud.purchase.client.service.RedisClusterService;
import cn.sipin.sales.cloud.request.CrsfTokenRequest;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;

@Aspect
@Order(100)
@Component
public class CsrfTokenAspect {

  public final static String PREFIX_CSRF_TOKEN = "csrftoken:";

  private RedisClusterService redisClusterService;

  @Autowired
  public CsrfTokenAspect(RedisClusterService redisClusterService) {
    this.redisClusterService = redisClusterService;
  }

  @Pointcut("execution(* cn.sipin.cloud.purchase.client.controller.*.*(..))")
  private void aspectController() {}

  @Before(value = "aspectController() && @annotation(cn.sipin.cloud.purchase.client.annotation.CsrfToken)")
  public void checkCrsfToken(JoinPoint point) throws Throwable {

    Object[] args = point.getArgs();
    if (Objects.isNull(args) || args.length == 0) {
      throw new RequestException(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "请求参数不存在");
    }
    String crsfToken = null;
    for (Object arg : args) {
      if (arg instanceof CrsfTokenRequest) {
        CrsfTokenRequest crsfTokenRequest = (CrsfTokenRequest) arg;
        crsfToken = crsfTokenRequest.getCrsfToken();
        break;
      }
    }

    if (crsfToken == null) {
      throw new RequestException(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "请求中无crsf token信息");
    }

    if (redisClusterService.del(crsfToken)) {
      // 删除成功 证明有crsf token ，允许通过
      return;
    }

    throw new RequestException(ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getValue(), "crsf token无效或过期");
  }
}
