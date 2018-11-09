package cn.sipin.sales.cloud.purchase.service.controller.front;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import javax.validation.Valid;

import cn.sipin.sales.cloud.constants.PurchaseOrderStatus;
import cn.sipin.sales.cloud.pojo.PurchaseOrder;
import cn.sipin.sales.cloud.pojo.PurchaseOrderConsignee;
import cn.sipin.sales.cloud.pojo.PurchaseOrderDetail;
import cn.sipin.sales.cloud.purchase.service.config.ErpConfig;
import cn.sipin.sales.cloud.purchase.service.feign.impl.MaterialServiceImpl;
import cn.sipin.sales.cloud.purchase.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.sales.cloud.purchase.service.feign.request.PurchasePaymentRequest;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderConsigneeServiceContract;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderServiceContract;
import cn.sipin.sales.cloud.request.PurchaseOrderListRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderNoteRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderRequest;
import cn.sipin.sales.cloud.request.PurchasePaymentAffirmRequest;
import cn.sipin.sales.cloud.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.response.MaterialResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;
import cn.sipin.sales.cloud.response.vo.OrderConsigneeVo;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;
import cn.siyue.platform.httplog.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前台采购单 控制器
 * </p>
 */
@RestController
@Api(tags = "前台采购订单")
@RequestMapping(path = "/front/purchase-order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PurchaseOrderController {

  private final static Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

  private MaterialServiceImpl materialService;

  private PurchaseOrderServiceContract purchaseOrderService;

  private SalesUserServiceImpl salesUserService;

  private PurchaseOrderConsigneeServiceContract purchaseOrderConsigneeService;

  private ErpConfig erpConfig;

  @Autowired
  public PurchaseOrderController(
      MaterialServiceImpl materialService, PurchaseOrderServiceContract purchaseOrderService, SalesUserServiceImpl salesUserService,
      PurchaseOrderConsigneeServiceContract purchaseOrderConsigneeService,
      ErpConfig erpConfig
  ) {
    this.materialService = materialService;
    this.purchaseOrderService = purchaseOrderService;
    this.salesUserService = salesUserService;
    this.purchaseOrderConsigneeService = purchaseOrderConsigneeService;
    this.erpConfig = erpConfig;
  }

  @PostMapping(value = "/list")
  @ApiOperation(nickname = "frontGetPurchaseOrders", value = "前台获取门店采购单列表")
  public ResponseData<Page<PurchaseOrderResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "size", required = false, defaultValue = "15") int size,
      @RequestBody PurchaseOrderListRequest request
  ) {
    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    Page<PurchaseOrderResponse> orderVoPage = new Page<>(page, size);
    orderVoPage.setAsc(false);

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    purchaseOrder.setShopCode(agencyInfoVo.getShopCode());

    orderVoPage = purchaseOrderService.selectOrderPage(purchaseOrder, orderVoPage, request,false);

    return new ResponseData<Page<PurchaseOrderResponse>>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        orderVoPage
    );
  }

  @ApiOperation(nickname = "frontShowPurchaseOrder", value = "前台获取采购单详情")
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
    purchaseOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    purchaseOrder.setShopCode(agencyInfoVo.getShopCode());
    purchaseOrder.setNo(orderNo);

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

    return new ResponseData<PurchaseOrderResponse>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        orderVo
    );
  }

  @ApiOperation(nickname = "frontCreatePurchaseOrder", value = "前台新建采购单")
  @PostMapping("/create")
  @LogAnnotation
  public ResponseData<PurchaseOrderResponse> store(@RequestBody PurchaseOrderRequest purchaseOrderRequest) {

    // 得到经销商信息 同步操作
    // AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();
    Future<ResponseData> future = null;
    Boolean isAsyn = true;
    try {
      // 异步操作
      future = salesUserService.getUserFutureByToken();
    } catch (RejectedExecutionException e) {
      logger.warn("异常信息：RejectedExecutionException" + e.getMessage());
      isAsyn = false;
    }

    List<String> noList = new ArrayList<>(purchaseOrderRequest.getDetails().size());
    purchaseOrderRequest.getDetails().forEach(it -> noList.add(it.getSkuNo()));
    String skuNos = String.join(",", noList);
    int requestDetailSize = purchaseOrderRequest.getDetails().size();
    // 排除添加相同SKU条目
    HashSet<String> skuNoSet = new HashSet<String>(requestDetailSize);
    purchaseOrderRequest.getDetails().forEach(it -> skuNoSet.add(it.getSkuNo()));

    // 获取SKU列表
    List<MaterialResponse> materialResponseList = materialService.getSkus(skuNos, skuNoSet.size());

    // 获取键值对(skuNo MaterialResponse) 的HashMap
    HashMap<String, MaterialResponse> materialResponseHashMap = new HashMap<>(materialResponseList.size());
    materialResponseList.forEach(it -> materialResponseHashMap.put(it.getNumber(), it));

    List<PurchaseOrderDetail> details = new ArrayList<>(requestDetailSize);
    purchaseOrderRequest.getDetails().forEach(it -> {
      PurchaseOrderDetail detail = new PurchaseOrderDetail();
      MaterialResponse materialResponse = materialResponseHashMap.get(it.getSkuNo());
      BeanUtils.copyProperties(materialResponse, detail);
      detail.setSkuNo(materialResponse.getNumber());
      detail.setSkuSn(materialResponse.getSku());
      detail.setTotal(it.getQuantity());
      detail.setNote(it.getNote());
      details.add(detail);
    });

    AgencyCodeResponse agencyInfoVo = null;
    if (isAsyn) {

      agencyInfoVo = salesUserService.getAgencyCodeResponseByFuture(future);
    } else {
      // 得到经销商信息 同步操作
      agencyInfoVo = salesUserService.getUserByToken();
    }
    // 异步操作 获取经销商信息

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    purchaseOrder.setShopCode(agencyInfoVo.getShopCode());
    purchaseOrder.setCreatorId(Long.valueOf(agencyInfoVo.getUserId()));
    purchaseOrder.setStatusId(PurchaseOrderStatus.WAIT_PAY.getCode());
    purchaseOrder.setNote(purchaseOrderRequest.getNote());
    // 经销商折扣
    purchaseOrder.setDiscount(new BigDecimal(agencyInfoVo.getAgencyDiscount()));
    // 把创建时间和更新时间也带上
    LocalDateTime now = LocalDateTime.now();
    purchaseOrder.setCreatedAt(now);
    purchaseOrder.setUpdatedAt(now);

    if (purchaseOrder.getDiscount().compareTo(BigDecimal.ZERO) <= 0) {
      return new ResponseData<PurchaseOrderResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "经销商折扣有误，不能小于等于0"
      );
    }

    // 创建
    PurchaseOrder order = null;
    try {
      order = purchaseOrderService.create(purchaseOrder, details, purchaseOrderRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }

    PurchaseOrderResponse orderVo = new PurchaseOrderResponse();

    BeanUtils.copyProperties(order, orderVo);

    orderVo.setAgencyName(agencyInfoVo.getAgencyName());
    orderVo.setShopName(agencyInfoVo.getShopName());
    orderVo.setCreatorName(agencyInfoVo.getUserName());
    orderVo.setNote(purchaseOrderRequest.getNote());

    return new ResponseData<PurchaseOrderResponse>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        orderVo
    );
  }

  @ApiOperation(nickname = "frontUpdatePurchaseOrderNote", value = "前台更新备注")
  @PutMapping("/update/{orderNo}/note")
  @LogAnnotation
  public ResponseData updateNote(
      @PathVariable(value = "orderNo") String orderNo,
      @RequestBody PurchaseOrderNoteRequest noteRequest
  ) {
    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();
    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    purchaseOrder.setShopCode(agencyInfoVo.getShopCode());
    purchaseOrder.setNo(orderNo);

    purchaseOrder = purchaseOrderService.selectOne(new EntityWrapper<>(purchaseOrder));
    if (purchaseOrder == null || purchaseOrder.getId() == null) {
      throw new RequestException(ResponseBackCode.ERROR_PARAM_INVALID.getValue(), ResponseBackCode.ERROR_PARAM_INVALID.getMessage());
    }

    PurchaseOrder tempPurchaseOrder = new PurchaseOrder();
    tempPurchaseOrder.setId(purchaseOrder.getId());
    tempPurchaseOrder.setNote(noteRequest.getNote());
    purchaseOrderService.updateById(tempPurchaseOrder);

    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage()
    );
  }

  @ApiOperation(nickname = "frontCancelPurchaseOrder", value = "前台取消采购单")
  @PutMapping("/cancel/{orderNo}")
  @LogAnnotation
  public ResponseData cancel(@PathVariable(value = "orderNo") String orderNo) {
    // 判断是否有权限删除，删除也是软删除。只有等待付款才能软删除

    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    PurchaseOrder purchaseOrder = purchaseOrderService.selectOne(new EntityWrapper<>(
        new PurchaseOrder(
            orderNo,
            agencyInfoVo.getAgencyCode(),
            agencyInfoVo.getShopCode(),
            PurchaseOrderStatus.WAIT_PAY.getCode()
        )));

    if (purchaseOrder == null) {
      return ResponseData.build(
          ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(),
          ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getMessage()
      );
    }

    PurchaseOrder tempOrder = new PurchaseOrder();
    tempOrder.setId(purchaseOrder.getId());
    tempOrder.setStatusId(PurchaseOrderStatus.CANCELED.getCode());
    Boolean isSuccess = purchaseOrderService.updateById(tempOrder);

    if (isSuccess) {
      return ResponseData.build(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage()
      );
    } else {
      return ResponseData.build(
          ResponseBackCode.ERROR_FAIL.getValue(),
          "取消失败"
      );
    }
  }

  @ApiOperation(nickname = "frontPurchasePaymentAffirm", value = "前台采购单支付确认")
  @PostMapping("/purchasePayment")
  @LogAnnotation
  ResponseData purchasePayment(@RequestBody @Valid PurchasePaymentAffirmRequest request) {
    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    PurchaseOrder purchaseOrder = purchaseOrderService.selectOne(
        new EntityWrapper<>(
            new PurchaseOrder(
                request.getPurchaseOrderNo(),
                agencyInfoVo.getAgencyCode(),
                agencyInfoVo.getShopCode(),
                PurchaseOrderStatus.WAIT_PAY.getCode()
            )));

    if (purchaseOrder == null || StringUtils.isBlank(request.getPurchaseOrderNo())) {
      return ResponseData.build(
          ResponseBackCode.ERROR_FAIL.getValue(),
          "删除失败:订单号不存在或经销商信息有误或订单状态异常"
      );
    }

    // 每次都提前更新"更新时间"是为了补偿机制可以筛选到
    PurchaseOrder updateDateOrder = new PurchaseOrder();
    updateDateOrder.setId(purchaseOrder.getId());
    updateDateOrder.setUpdatedAt(LocalDateTime.now());
    purchaseOrderService.updateById(updateDateOrder);
    updateDateOrder = null;

    PurchasePaymentRequest purchasePaymentRequest = new PurchasePaymentRequest();
    purchasePaymentRequest.setPurchaseOrderNo(purchaseOrder.getNo());
    purchasePaymentRequest.setAmount(purchaseOrder.getAmount());
    // TODO 在这里需要记录支付正在进行中,例如把"orderNo:进行中"记录在redis或数据库上，方便之后可能出现付款成功，但没有更新订单状态时，可用补偿机制重新获取付款信息，然后更新状态
    String paymentNo = salesUserService.purchasePayment(purchasePaymentRequest);
    logger.info("经销商({})-门店({})采购订单({})支付成功，支付流水号为({})", agencyInfoVo.getAgencyCode(), agencyInfoVo.getShopCode(), purchaseOrder.getNo(), paymentNo);

    PurchaseOrder tempPurchaseOrder = new PurchaseOrder();
    tempPurchaseOrder.setId(purchaseOrder.getId());
    tempPurchaseOrder.setAuditorId(Long.valueOf(agencyInfoVo.getUserId()));
    tempPurchaseOrder.setAuditedAt(LocalDateTime.now());
    tempPurchaseOrder.setStatusId(PurchaseOrderStatus.PAY_SUCCESSFULLY.getCode());
    // TODO 以后有优惠券 要改
    tempPurchaseOrder.setPaidAmount(purchaseOrder.getPayableAmount());
    purchaseOrderService.paySuccessFully(tempPurchaseOrder);

    return ResponseData.build(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage()
    );
  }

  @ApiOperation(nickname = "frontDeletePurchaseOrder", value = "前台删除采购单")
  @PutMapping("/delete/{orderNo}")
  @LogAnnotation
  public ResponseData destroy(@PathVariable String orderNo) {
    // 判断是否有权限删除，删除也是软删除。只有等待付款才能软删除

    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    Boolean isSuccess = purchaseOrderService.delete(
        new EntityWrapper<>(
            new PurchaseOrder(
                orderNo,
                agencyInfoVo.getAgencyCode(),
                agencyInfoVo.getShopCode(),
                PurchaseOrderStatus.WAIT_PAY.getCode()
            )));

    if (isSuccess) {
      return ResponseData.build(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage()
      );
    } else {
      return ResponseData.build(
          ResponseBackCode.ERROR_FAIL.getValue(),
          "删除失败"
      );
    }
  }
}

