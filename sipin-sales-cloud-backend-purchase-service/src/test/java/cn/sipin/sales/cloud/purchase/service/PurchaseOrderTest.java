/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service;

import com.baomidou.mybatisplus.plugins.Page;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import cn.sipin.sales.cloud.purchase.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderServiceContract;
import cn.sipin.sales.cloud.purchase.service.service.RedisClusterServiceContract;
import cn.sipin.sales.cloud.request.ErpOrdersRequest;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("develop")
public class PurchaseOrderTest {

  @Autowired
  private DiscoveryClient discoveryClient;

  @Autowired
  private RedisClusterServiceContract redisClusterService;

  @Autowired
  private PurchaseOrderServiceContract purchaseOrderService;

  @Autowired
  private SalesUserServiceImpl salesUserService;

  @Test
  public void packageTest() {

    String packageName = PurchaseOrderTest.class.getPackage().getName();
    System.out.println(packageName);
  }

  @Test
  public void getServiceId() {

    List<ServiceInstance> serviceInstances = discoveryClient.getInstances("merchandise-client");

    serviceInstances.forEach(it -> {
      System.out.println("host=" + it.getHost());
      System.out.println("url=" + it.getUri());
      System.out.println("port=" + it.getPort());
    });
  }

  @Test
  public void setAndGetRedis() {

    String key = "martin6699111";
    redisClusterService.set(key, "39389398939393");

    System.out.println("获取得到的value: " + redisClusterService.get(key));
  }

  @Test
  public void setHashAndGetRedis() {
    String key = "martin669933";
    redisClusterService.hset(key, "name", "39389398939393");

    System.out.println("获取得到的value: " + redisClusterService.get(key));
  }

  @Test
  public void getErpOrders() {

    Page<PurchaseOrderResponse> orderVoPage = new Page<>(1, 50);
    orderVoPage.setAsc(false);
    ErpOrdersRequest request = new ErpOrdersRequest();
    request.setBeginDate("2018-07-01");
    request.setEndDate("2018-07-12");

    purchaseOrderService.selectOrderForErpPage(orderVoPage, request);
  }

  private static final int counts = 10000000;

  @Test
  public void testRandomStringUtils() {
    long startTime = System.currentTimeMillis();
    String crsfToKenKey = "";
    for (int i = 1; i < counts; i++) {
      crsfToKenKey = "PREFIX_CRSF_TOKEN" + RandomStringUtils.randomAlphanumeric(22);
    }

    System.out.println(crsfToKenKey);

    long endTime = System.currentTimeMillis();

    // 程序运行时间： 4607ms
    System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

    System.out.println("开始时间："  + startTime);
    System.out.println("结束时间：" + endTime);
  }


  @Test
  public void testUtils() {
    long startTime = System.currentTimeMillis();

    for (int i = 1; i < counts; i++) {
      String crsfToKenKey = "PREFIX_CRSF_TOKEN" + UUID.randomUUID().toString();
    }

    long endTime = System.currentTimeMillis();
    // 程序运行时间： 18716ms
    // 程序运行时间： 17514ms
    System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
  }

  @Test
  public void testGetAllAgencyOuterCode() {
    salesUserService.getAllAgencyOuterCodeMap();
  }

  @Test
  public void testGetAllAgencyAndShopName() {
    salesUserService.getAllAgencyAndShopNameAndSourceMap();
  }

}
