/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfigInterceptor implements RequestInterceptor {

  @Override public void apply(RequestTemplate template) {

    HttpServletRequest request = getHttpServletRequest();

    if (Objects.isNull(request)) {
      return;
    }

    Map<String, String> headers = getHeaders(request);
    if (headers.size() > 0) {
      Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
      while (iterator.hasNext()) {
        Entry<String, String> entry = iterator.next();
        template.header(entry.getKey(), entry.getValue());
      }
    }
  }

  private HttpServletRequest getHttpServletRequest() {
    try {
      return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    } catch (Exception e) {
      return null;
    }
  }

  private Map<String, String> getHeaders(HttpServletRequest request) {
    Map<String, String> map = new LinkedHashMap<>();
    Enumeration<String> enumeration = request.getHeaderNames();
    while (enumeration.hasMoreElements()) {
      String key = enumeration.nextElement();
      String value = request.getHeader(key);
      map.put(key, value);
    }
    return map;
  }
}
