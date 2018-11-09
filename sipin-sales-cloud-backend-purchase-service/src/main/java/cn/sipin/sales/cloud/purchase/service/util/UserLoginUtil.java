package cn.sipin.sales.cloud.purchase.service.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import cn.sipin.sales.cloud.purchase.service.service.RedisClusterServiceContract;

@Component
public class UserLoginUtil {

  public static final String REDIS_USER_SESSION_KEY = "REDIS_USER_SESSION";

  private RedisClusterServiceContract redisService;

  @Autowired
  public UserLoginUtil(RedisClusterServiceContract redisService) {
    this.redisService = redisService;
  }

  public Long getUserId() {
    try {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
      String token = request.getHeader("token");
      String userId = null;
      if (StringUtils.isNotEmpty(token)) {
        userId = redisService.get(REDIS_USER_SESSION_KEY + ":" + token);
      }
      if (StringUtils.isNotBlank(userId)) {
        return new Long(userId);
      }

    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }
}
