package cn.sipin.sales.cloud.purchase.service.controller.front;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

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
@Api(tags = "采购订单")
@RequestMapping(path = "/front/purchase-order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PurchaseOrderDetailController {

  private SalesUserServiceImpl salesUserService;

  private PurchaseOrderServiceContract purchaseOrderService;

  private PurchaseOrderDetailServiceContract purchaseOrderDetailService;

  @Autowired
  public PurchaseOrderDetailController(
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
  public ResponseData index(
      @PathVariable(value = "orderNo") String orderNo
  ) {
    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setNo(orderNo);
    purchaseOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    purchaseOrder.setShopCode(agencyInfoVo.getShopCode());

    purchaseOrder = purchaseOrderService.selectOne(
        new EntityWrapper<>(purchaseOrder));

    if (Objects.isNull(purchaseOrder) || Objects.isNull(purchaseOrder.getId())) {
      return ResponseData.build(
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

    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        targetDetailVos
    );
  }

//  @ApiOperation(nickname = "createPurchaseDetail", value = "新建采购单详情")
//  @PostMapping("/{orderNo}/detail")
//  public ResponseData store(
//      @PathVariable(value = "orderNo") String orderNo,
//      @RequestBody PurchaseOrderDetailRequest request
//  ) {
//
//    // 得到经销商信息
//    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();
//
//    PurchaseOrder order = purchaseOrderService.selectOne(new EntityWrapper<>(new PurchaseOrder(orderNo)));
//
//    if (order == null) {
//      throw new RequestException(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "采购订单找不到");
//    }
//
//    if (!(order.getAgencyCode().equals(agencyInfoVo.getAgencyCode()) &&
//          order.getShopCode().equals(agencyInfoVo.getShopCode()) &&
//          order.getStatusId().equals(PurchaseOrderStatus.WAIT_PAY.getCode())
//    )) {
//      throw new RequestException(ResponseBackCode.ERROR_CREATE_FAIL.getValue(), "订单必须处于未付款状态才能添加详情");
//    }
//
//    // 获取SKU信息
//    List<MaterialResponse> materialResponseList = materialService.getSkus(request.getSkuNo(), 1);
//
//    PurchaseOrderDetail detail = new PurchaseOrderDetail();
//    materialResponseList.forEach(it -> {
//      BeanUtils.copyProperties(it, detail);
//      detail.setNo(it.getNumber());
//      detail.setSkuSn(it.getSku());
//      detail.setTotal(request.getQuantity());
//      detail.setNote(request.getNote());
//      detail.setPurchaseOrderId(order.getId());
//    });
//
//    Boolean isSuccess = purchaseOrderDetailService.insert(detail);
//
//    return ResponseData.build(
//        ResponseBackCode.SUCCESS.getValue(),
//        ResponseBackCode.SUCCESS.getMessage(),
//        isSuccess
//    );
//  }
//
//  @ApiOperation(nickname = "deletePurchaseDetail", value = "删除采购单详情")
//  @PutMapping("/{orderNo}/detail/delete/{detailId}")
//  public ResponseData destroy(@PathVariable String orderNo, @PathVariable Long detailId) {
//    // TODO 需要判断是否有权限删除，删除也是软删除。只有等待付款才能软删除
//    // 得到经销商信息
//    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();
//
//    PurchaseOrder purchaseOrder = purchaseOrderService.selectOne(
//        new EntityWrapper<>(
//            new PurchaseOrder(
//                orderNo,
//                agencyInfoVo.getAgencyCode(),
//                agencyInfoVo.getShopCode(),
//                PurchaseOrderStatus.WAIT_PAY.getCode()
//            )));
//
//    if (purchaseOrder == null) {
//      return ResponseData.build(
//          ResponseBackCode.ERROR_FAIL.getValue(),
//          "删除失败:该订单详情不满足删除条件"
//      );
//    }
//
//    // 删除
//    purchaseOrderDetailService.deleteById(detailId);
//
//    return ResponseData.build(
//        ResponseBackCode.SUCCESS.getValue(),
//        ResponseBackCode.SUCCESS.getMessage()
//    );
//  }
}

