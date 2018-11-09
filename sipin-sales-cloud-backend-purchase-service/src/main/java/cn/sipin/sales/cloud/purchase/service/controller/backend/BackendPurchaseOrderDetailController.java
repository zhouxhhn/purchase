package cn.sipin.sales.cloud.purchase.service.controller.backend;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.sipin.sales.cloud.constants.PurchaseOrderStatus;
import cn.sipin.sales.cloud.pojo.PurchaseOrder;
import cn.sipin.sales.cloud.pojo.PurchaseOrderDetail;
import cn.sipin.sales.cloud.purchase.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderDetailServiceContract;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderServiceContract;
import cn.sipin.sales.cloud.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 采购单明细 前端控制器
 * </p>
 */
@RestController
@Api(tags = "后台采购订单")
@RequestMapping(path = "/backend/purchase-order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BackendPurchaseOrderDetailController {


  private SalesUserServiceImpl salesUserService;

  private PurchaseOrderServiceContract purchaseOrderService;

  private PurchaseOrderDetailServiceContract purchaseOrderDetailService;

  @Autowired
  public BackendPurchaseOrderDetailController(
      PurchaseOrderServiceContract purchaseOrderService,
      PurchaseOrderDetailServiceContract purchaseOrderDetailService,
      SalesUserServiceImpl salesUserService
  ) {
    this.purchaseOrderService = purchaseOrderService;
    this.purchaseOrderDetailService = purchaseOrderDetailService;
    this.salesUserService = salesUserService;
  }

  @GetMapping("/{orderNo}/detail")
  @ApiOperation(nickname = "getPurchaseOrderDetails", value = "获取门店采购单详情")
  public ResponseData<List<PurchaseOrderDetailResponse>> index(
      @PathVariable(value = "orderNo") String orderNo
  ) {
    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setNo(orderNo);

    if (StringUtils.isNotBlank(agencyInfoVo.getShopCode())) {
      // 如果门店为空或者空字符串，则是斯品内部管理员，可查询所有采购订单详情
      // 否则 为经销商管理员可查 自己的经销商门店订单
      purchaseOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
      purchaseOrder.setShopCode(agencyInfoVo.getShopCode());
    }


    purchaseOrder = purchaseOrderService.selectOne(
        new EntityWrapper<>(purchaseOrder));

    if (Objects.isNull(purchaseOrder) || Objects.isNull(purchaseOrder.getId())) {
      return new ResponseData<List<PurchaseOrderDetailResponse>>(
          ResponseBackCode.ERROR_FAIL.getValue(),
          "订单不存在"
      );
    }
    List<PurchaseOrderDetail> sourceDetails = purchaseOrderDetailService.selectList(
        new EntityWrapper<>(
            new PurchaseOrderDetail(purchaseOrder.getId())));

    List<PurchaseOrderDetailResponse> targetDetailVos = new ArrayList<>(sourceDetails.size());

    sourceDetails.forEach(it -> {
      PurchaseOrderDetailResponse detailResponse = new PurchaseOrderDetailResponse();
      BeanUtils.copyProperties(it, detailResponse);
      targetDetailVos.add(detailResponse);
    });

    return new ResponseData<List<PurchaseOrderDetailResponse>>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        targetDetailVos
    );
  }

}

