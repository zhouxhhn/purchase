package cn.sipin.sales.cloud.purchase.service.controller.backend;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.sipin.sales.cloud.pojo.PurchaseOrder;
import cn.sipin.sales.cloud.pojo.PurchaseOrderConsignee;
import cn.sipin.sales.cloud.pojo.PurchaseOrderExpress;
import cn.sipin.sales.cloud.purchase.service.config.ErpConfig;
import cn.sipin.sales.cloud.purchase.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderConsigneeServiceContract;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderExpressServiceContract;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderServiceContract;
import cn.sipin.sales.cloud.request.PurchaseAddMaterialRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderListRequest;
import cn.sipin.sales.cloud.request.PurchaseUpdateMaterialRequest;
import cn.sipin.sales.cloud.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderExpressResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;
import cn.sipin.sales.cloud.response.vo.OrderConsigneeVo;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 采购单 控制器
 * </p>
 */
@RestController
@Api(tags = "后台采购订单")
@RequestMapping(path = "/backend/purchase-order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BackendPurchaseOrderController {

  private PurchaseOrderServiceContract purchaseOrderService;

  private SalesUserServiceImpl salesUserService;

  private PurchaseOrderConsigneeServiceContract purchaseOrderConsigneeService;

  private PurchaseOrderExpressServiceContract purchaseOrderExpressService;

  private ErpConfig erpConfig;

  public BackendPurchaseOrderController(
      PurchaseOrderServiceContract purchaseOrderService, SalesUserServiceImpl salesUserService,
      PurchaseOrderConsigneeServiceContract purchaseOrderConsigneeService,
      PurchaseOrderExpressServiceContract purchaseOrderExpressService,
      ErpConfig erpConfig
  ) {
    this.purchaseOrderService = purchaseOrderService;
    this.salesUserService = salesUserService;
    this.purchaseOrderConsigneeService = purchaseOrderConsigneeService;
    this.purchaseOrderExpressService = purchaseOrderExpressService;
    this.erpConfig = erpConfig;
  }

  @PostMapping(value = "/list")
  @ApiOperation(nickname = "backendGetPurchaseOrders", value = "后台获取门店采购单列表")
  public ResponseData<Page<PurchaseOrderResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "size", required = false, defaultValue = "15") int size,
      @RequestBody PurchaseOrderListRequest request
  ) {
    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    Page<PurchaseOrderResponse> orderVoPage = new Page<>(page, size);

    if (StringUtils.isBlank(agencyInfoVo.getShopCode())) {
      // 如果门店为空或者空字符串，则是斯品内部管理员，可查询所有采购订单

      orderVoPage = purchaseOrderService.selectOrderPage(null, orderVoPage, request,false);

      return new ResponseData<Page<PurchaseOrderResponse>>(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage(),
          orderVoPage
      );
    }

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    purchaseOrder.setShopCode(agencyInfoVo.getShopCode());

    if (!Objects.isNull(request.getShopCode())) {
      purchaseOrder.setShopCode(request.getShopCode());
    }

    orderVoPage = purchaseOrderService.selectOrderPage(purchaseOrder, orderVoPage, request,false);

    return new ResponseData<Page<PurchaseOrderResponse>>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        orderVoPage
    );
  }

  @ApiOperation(nickname = "showPurchaseOrder", value = "获取采购单详情")
  @GetMapping("/{orderNo}")
  public ResponseData<PurchaseOrderResponse> show(@PathVariable String orderNo) {
    if (StringUtils.isBlank(orderNo)) {
      return new ResponseData<PurchaseOrderResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          ResponseBackCode.ERROR_PARAM_INVALID.getMessage()
      );
    }

    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setNo(orderNo);

    if (StringUtils.isNotBlank(agencyInfoVo.getShopCode())) {
      // 如果门店为空或者空字符串，则是斯品内部管理员，可查询所有采购订单详情
      // 否则 为经销商管理员可查 只能查自己门店订单
      purchaseOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
      purchaseOrder.setShopCode(agencyInfoVo.getShopCode());
    }

    purchaseOrder = purchaseOrderService.selectOne(new EntityWrapper<>(purchaseOrder));
    List<PurchaseOrderDetailResponse> detailVos = purchaseOrderService.selectOrderDetail(purchaseOrder.getId());
    PurchaseOrderResponse orderVo = new PurchaseOrderResponse();
    // FIXME 需要获取经销商信息 把经销商code和管理员ID换成名称
    BeanUtils.copyProperties(purchaseOrder, orderVo);

    orderVo.setAgencyName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(purchaseOrder.getAgencyCode()));
    orderVo.setShopName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(purchaseOrder.getShopCode()));

    orderVo.setDetailVos(detailVos);

    // 地址信息
    PurchaseOrderConsignee consignee = purchaseOrderConsigneeService.selectOne(new EntityWrapper<>(new PurchaseOrderConsignee(purchaseOrder.getId())));
    OrderConsigneeVo orderConsignee = new OrderConsigneeVo();
    BeanUtils.copyProperties(consignee, orderConsignee);
    orderVo.setOrderConsignee(orderConsignee);

    PurchaseOrderExpress express = new PurchaseOrderExpress();
    express.setOrderNo(purchaseOrder.getNo());
    List<PurchaseOrderExpress> expressList = purchaseOrderExpressService.selectList(new EntityWrapper<>(express));
    List<PurchaseOrderExpressResponse> orderExpressesResponse;
    if (expressList.size() > 0) {
      orderExpressesResponse = new ArrayList<>(expressList.size());

      expressList.forEach(it -> {
        PurchaseOrderExpressResponse response = new PurchaseOrderExpressResponse();
        BeanUtils.copyProperties(it, response);
        orderExpressesResponse.add(response);
      });

      orderVo.setOrderExpressList(orderExpressesResponse);
    } else {
      orderVo.setOrderExpressList(new ArrayList<>(0));
    }

    return new ResponseData<PurchaseOrderResponse>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        orderVo
    );
  }

  @GetMapping("/materialDetail/{orderNo}")
  public ResponseData<Page<PurchaseOrderDetailResponse>> materialDetail(@PathVariable String orderNo) {

    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setNo(orderNo);

    if (StringUtils.isNotBlank(agencyInfoVo.getShopCode())) {
      // 如果门店为空或者空字符串，则是斯品内部管理员，可查询所有采购订单详情
      // 否则 为经销商管理员可查 只能查自己门店订单
      purchaseOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
      purchaseOrder.setShopCode(agencyInfoVo.getShopCode());
    }

    purchaseOrder = purchaseOrderService.selectOne(new EntityWrapper<>(purchaseOrder));
    List<PurchaseOrderDetailResponse> detailVos = purchaseOrderService.selectOrderDetail(purchaseOrder.getId());
    Page<PurchaseOrderDetailResponse> orderDetailResponsePage = new Page<>();
    if(detailVos != null && !detailVos.isEmpty()){
      orderDetailResponsePage.setSize(detailVos.size());
      orderDetailResponsePage.setTotal(detailVos.size());
      orderDetailResponsePage.setSize(detailVos.size());
    }
    orderDetailResponsePage.setRecords(detailVos);
    return new ResponseData<>(ResponseBackCode.SUCCESS.getValue(),ResponseBackCode.SUCCESS.getMessage(),orderDetailResponsePage);

  }

  /**
   * 新增采购订单中的商品
   */
  @PostMapping(value = "/addMaterial")
  public ResponseData addMaterial(@RequestBody PurchaseAddMaterialRequest request) {
    return purchaseOrderService.addMaterial(request);
  }

  /**
   * 新增采购订单中的商品
   */
  @PutMapping(value = "/deleteMaterial/{orderNo}")
  public ResponseData deleteMaterial(@PathVariable String orderNo) {
    return purchaseOrderService.deleteMaterial(orderNo);
  }

  /**
   * 新增采购订单中的商品
   */
  @PutMapping(value = "/updateMaterial/{orderNo}")
  public ResponseData updateMaterial(@PathVariable String orderNo,@RequestBody PurchaseUpdateMaterialRequest request) {
    return purchaseOrderService.updateMaterial(orderNo,request);
  }

  @PostMapping(value = "/exportExcel")
  @ApiOperation(nickname = "backendExportExcelPurchaseOrders", value = "后台导出门店采购单列表")
  public ResponseData index(@RequestBody PurchaseOrderListRequest request) {
    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    Page<PurchaseOrderResponse> orderVoPage = new Page<>();

    if (StringUtils.isBlank(agencyInfoVo.getShopCode())) {
      // 如果门店为空或者空字符串，则是斯品内部管理员，可查询所有采购订单

      orderVoPage = purchaseOrderService.selectOrderPage(null, orderVoPage, request,true);
      getExpressList(orderVoPage);
      return ResponseData.build( ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), orderVoPage.getRecords());
    }

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    purchaseOrder.setShopCode(agencyInfoVo.getShopCode());

    if (!Objects.isNull(request.getShopCode())) {
      purchaseOrder.setShopCode(request.getShopCode());
    }

    orderVoPage = purchaseOrderService.selectOrderPage(purchaseOrder, orderVoPage, request,true);
    getExpressList(orderVoPage);


    return ResponseData.build( ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), orderVoPage.getRecords() );
  }

  /**
   * 设置物流信息
   */
  private void getExpressList(Page<PurchaseOrderResponse> orderVoPage  ){
    if(orderVoPage.getRecords() != null && !orderVoPage.getRecords().isEmpty()){
      List<PurchaseOrderResponse> purchaseOrderResponseList = orderVoPage.getRecords();
      for (int i = 0,size = purchaseOrderResponseList.size(); i < size; i++) {
        PurchaseOrderResponse response = purchaseOrderResponseList.get(i);
        List<PurchaseOrderExpress> expressList = purchaseOrderExpressService.selectList(
            new EntityWrapper<>(
                new PurchaseOrderExpress(response.getNo())));
        List<PurchaseOrderExpressResponse> purchaseOrderDetailResponseList = new ArrayList<>(expressList.size());
        expressList.forEach(sourceDetail -> {
          PurchaseOrderExpressResponse targetDetailVo = new PurchaseOrderExpressResponse();
          BeanUtils.copyProperties(sourceDetail, targetDetailVo);
          purchaseOrderDetailResponseList.add(targetDetailVo);
        });
        response.setOrderExpressList(purchaseOrderDetailResponseList);
      }
    }
  }
}

