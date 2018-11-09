/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import cn.sipin.sales.cloud.constants.PurchaseOrderStatus;
import cn.sipin.sales.cloud.pojo.PurchaseOrder;
import cn.sipin.sales.cloud.purchase.service.config.ErpConfig;
import cn.sipin.sales.cloud.purchase.service.contract.Loggable;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderExpressServiceContract;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderServiceContract;
import cn.sipin.sales.cloud.purchase.service.util.JsonTransformer;
import cn.sipin.sales.cloud.request.ErpOrderExpressRequest;
import cn.sipin.sales.cloud.request.ErpOrdersRequest;
import cn.sipin.sales.cloud.request.ErpRequest;
import cn.sipin.sales.cloud.request.ErpUpdateOrderStatusRequest;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "ERP获取订单接口")
@RequestMapping(path = "/erp/purchase-orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ErpController implements Loggable {

  private PurchaseOrderServiceContract purchaseOrderService;

  private PurchaseOrderExpressServiceContract purchaseOrderExpressService;

  private ErpConfig erpConfig;

  @Autowired
  public ErpController(
      PurchaseOrderServiceContract purchaseOrderService, PurchaseOrderExpressServiceContract purchaseOrderExpressService,
      ErpConfig erpConfig
  ) {
    this.purchaseOrderService = purchaseOrderService;
    this.purchaseOrderExpressService = purchaseOrderExpressService;
    this.erpConfig = erpConfig;
  }

  @PostMapping(value = "")
  @ApiOperation(nickname = "getPurchaseOrdersForErp", value = "ERP获取门店采购单列表")
  public ResponseData index(
      @RequestParam(value = "sign") String sign,
      @RequestBody ErpOrdersRequest request
  ) {

    // 验证x-erp-token
    HttpServletRequest httpServletRequest = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
//    if (!erpConfig.getXErpToken().equals(httpServletRequest.getHeader("X-Erp-Token"))) {
//      throw new RequestException(ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getValue(), ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getMessage());
//    }
//
//    // 验证签名
//    String rawSign = sign(request, erpConfig.getSecret());
//    if (!rawSign.equals(sign)) {
//      throw new RequestException(ResponseBackCode.ERROR_AUTH_FAIL.getValue(), ResponseBackCode.ERROR_AUTH_FAIL.getMessage());
//    }

    Page<PurchaseOrderResponse> orderVoPage = new Page<>(request.getPage(), request.getPageSize());
    orderVoPage.setAsc(false);

    try {
      orderVoPage = purchaseOrderService.selectOrderForErpPage(orderVoPage, request);

      return ResponseData.build(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage(),
          orderVoPage
      );
    } catch (Exception e) {
      e.printStackTrace();
      throw new RequestException(ResponseBackCode.ERROR_AUTH_FAIL.getValue(), e.getMessage());
    }
  }

  @PostMapping("/status/update")
  @ApiOperation(nickname = "erpUpdatePurchaseOrderStatus", value = "ERP更新订单状态")
  public ResponseData updateOrderStatus(
      @RequestParam(value = "sign") String sign,
      @RequestBody @Valid ErpUpdateOrderStatusRequest request
  ) {
    // 验证x-erp-token
    HttpServletRequest httpServletRequest = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    if (!erpConfig.getXErpToken().equals(httpServletRequest.getHeader("X-Erp-Token"))) {
      throw new RequestException(ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getValue(), ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getMessage());
    }

    // 验证签名
    String rawSign = sign(request, erpConfig.getSecret());

    if (!rawSign.equals(sign)) {
      throw new RequestException(ResponseBackCode.ERROR_AUTH_FAIL.getValue(), ResponseBackCode.ERROR_AUTH_FAIL.getMessage());
    }

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setNo(request.getOrderNo());
    purchaseOrder = purchaseOrderService.selectOne(new EntityWrapper<>(purchaseOrder));
    if (Objects.isNull(request.getOrderNo()) || Objects.isNull(purchaseOrder) || Objects.isNull(purchaseOrder.getId())) {
      throw new RequestException(ResponseBackCode.ERROR_PARAM_INVALID.getValue(), "订单号" + ResponseBackCode.ERROR_PARAM_INVALID.getMessage());
    }

    if (purchaseOrder.getStatusId().compareTo(request.getTargertOrderStatus()) >= 0) {
      // 如果要更新的目标状态小于等于本地订单状态，说明已更新过了
      return ResponseData.build(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage(),
          true
      );
    }

    if (purchaseOrder.getStatusId().equals(request.getSourceOrderStatus())) {

      PurchaseOrder tempOrder = new PurchaseOrder();
      tempOrder.setId(purchaseOrder.getId());
      tempOrder.setStatusId(request.getTargertOrderStatus());
      logger().info("ERP更新订单状态-请求参数：{}", JsonTransformer.toJson(request));
      Boolean isSuccess = purchaseOrderService.updateById(tempOrder);
      logger().info(
          "订单号为{}的订单，状态从{}更新为{}", request.getOrderNo(), PurchaseOrderStatus.getEnum(request.getSourceOrderStatus()), PurchaseOrderStatus.getEnum(request.getTargertOrderStatus()));

      if (isSuccess) {
        return ResponseData.build(
            ResponseBackCode.SUCCESS.getValue(),
            ResponseBackCode.SUCCESS.getMessage(),
            true
        );
      }
    } else {
      return ResponseData.build(
          ResponseBackCode.ERROR_UPDATE_FAIL.getValue(),
          ResponseBackCode.ERROR_UPDATE_FAIL.getMessage() + ": 请求数据SourceOrderStatus与本地数据库订单状态不一致",
          false
      );
    }

    return ResponseData.build(
        ResponseBackCode.ERROR_UPDATE_FAIL.getValue(),
        ResponseBackCode.ERROR_UPDATE_FAIL.getMessage(),
        false
    );
  }

  @PostMapping("/express/create")
  @ApiOperation(nickname = "createExpressForErp", value = "ERP推送物流信息")
  public ResponseData createExpress(
      @RequestParam(value = "sign") String sign,
      @RequestBody @Valid ErpOrderExpressRequest request
  ) {
    // 验证x-erp-token
    HttpServletRequest httpServletRequest = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    if (!erpConfig.getXErpToken().equals(httpServletRequest.getHeader("X-Erp-Token"))) {
      throw new RequestException(ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getValue(), ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getMessage());
    }

    // 验证签名
    String rawSign = sign(request, erpConfig.getSecret());

    if (!rawSign.equals(sign)) {
      throw new RequestException(ResponseBackCode.ERROR_AUTH_FAIL.getValue(), ResponseBackCode.ERROR_AUTH_FAIL.getMessage());
    }

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setNo(request.getOrderNo());
    purchaseOrder = purchaseOrderService.selectOne(new EntityWrapper<>(purchaseOrder));

    if (Objects.isNull(request.getOrderNo()) || Objects.isNull(purchaseOrder) || Objects.isNull(purchaseOrder.getId())) {
      throw new RequestException(ResponseBackCode.ERROR_PARAM_INVALID.getValue(), "订单号" + ResponseBackCode.ERROR_PARAM_INVALID.getMessage());
    }

    logger().info("ERP推送物流信息-请求参数：{}", JsonTransformer.toJson(request));
      
    Boolean isSuccess = purchaseOrderExpressService.create(purchaseOrder, request);
    if (isSuccess) {
      return ResponseData.build(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage(),
          true
      );
    }

    return ResponseData.build(
        ResponseBackCode.ERROR_CREATE_FAIL.getValue(),
        ResponseBackCode.ERROR_CREATE_FAIL.getMessage(),
        false
    );
  }

  private String sign(ErpRequest request, String secret) {
    StringBuilder sign = new StringBuilder(secret);
    Map params = request.toParams();
    Iterator paramsIterator = params.entrySet().iterator();

    String key;
    while (paramsIterator.hasNext()) {
      Entry entry = (Entry) paramsIterator.next();
      key = (String) entry.getKey();
      String value = (String) entry.getValue();
      if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
        sign.append(key).append(value);
      }
    }

    sign.append(secret);

    MessageDigest md5;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    Charset charset = Charset.forName("UTF-8");

    if (charset == null) {
      throw new RuntimeException("没有找到UTF-8字符集");
    }

    byte[] bytes = sign.toString().getBytes(charset);

    md5.update(bytes);

    String hexBinary = DatatypeConverter.printHexBinary(md5.digest());

    if (hexBinary == null) {
      throw new RuntimeException("hexBinary为空");
    }
    // 注意转大写
    return hexBinary.toUpperCase();
  }
}
