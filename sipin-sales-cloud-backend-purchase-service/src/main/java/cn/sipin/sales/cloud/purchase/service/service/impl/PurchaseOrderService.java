package cn.sipin.sales.cloud.purchase.service.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sipin.sales.cloud.constants.PurchaseOrderMaterialStatus;
import cn.sipin.sales.cloud.constants.PurchaseOrderStatus;
import cn.sipin.sales.cloud.pojo.PurchaseOrder;
import cn.sipin.sales.cloud.pojo.PurchaseOrderConsignee;
import cn.sipin.sales.cloud.pojo.PurchaseOrderDetail;
import cn.sipin.sales.cloud.pojo.PurchaseOrderExpress;
import cn.sipin.sales.cloud.purchase.service.config.ErpConfig;
import cn.sipin.sales.cloud.purchase.service.contract.Loggable;
import cn.sipin.sales.cloud.purchase.service.feign.service.MaterialService;
import cn.sipin.sales.cloud.purchase.service.feign.service.SalesUserService;
import cn.sipin.sales.cloud.purchase.service.mapper.PurchaseOrderMapper;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderConsigneeServiceContract;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderExpressServiceContract;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderServiceContract;
import cn.sipin.sales.cloud.purchase.service.util.GenerateDistributedID;
import cn.sipin.sales.cloud.purchase.service.util.JsonTransformer;
import cn.sipin.sales.cloud.request.ErpOrdersRequest;
import cn.sipin.sales.cloud.request.PurchaseAddMaterialRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderListRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderRequest;
import cn.sipin.sales.cloud.request.PurchaseUpdateMaterialRequest;
import cn.sipin.sales.cloud.request.vo.AddressVo;
import cn.sipin.sales.cloud.response.IndexUserResponse;
import cn.sipin.sales.cloud.response.MaterialResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderExpressResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;
import cn.sipin.sales.cloud.response.vo.OrderConsigneeVo;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

/**
 * <p>
 * 采购单 服务实现类
 * </p>
 */
@Service
public class PurchaseOrderService extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderServiceContract, Loggable {

  private PurchaseOrderDetailService purchaseOrderDetailService;

  private PurchaseOrderConsigneeServiceContract purchaseOrderConsigneeService;

  private ErpConfig erpConfig;

  private MaterialService materialService;

  private SalesUserService salesUserService;



  @Autowired
  public PurchaseOrderService(
      PurchaseOrderDetailService purchaseOrderDetailService, PurchaseOrderConsigneeServiceContract purchaseOrderConsigneeService,
      ErpConfig erpConfig,MaterialService materialService,SalesUserService salesUserService
  ) {
    this.purchaseOrderDetailService = purchaseOrderDetailService;
    this.purchaseOrderConsigneeService = purchaseOrderConsigneeService;
    this.erpConfig = erpConfig;
    this.materialService = materialService;
    this.salesUserService = salesUserService;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public PurchaseOrder create(PurchaseOrder order, List<PurchaseOrderDetail> details, PurchaseOrderRequest purchaseOrderRequest) {

    BigDecimal total = new BigDecimal("0.00");
    for (PurchaseOrderDetail detail : details) {
      BigDecimal orderSkuTotal = detail.getAmount().multiply(new BigDecimal(detail.getTotal().toString()), MathContext.DECIMAL128);
      total = total.add(orderSkuTotal);
    }
    // 乘经销商折扣
    total = total.multiply(order.getDiscount(), MathContext.DECIMAL128);
    // 保存小数点后2位 四舍五入向上舍人
    total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
    order.setAmount(total);
    order.setPayableAmount(total);
    order.setNo(GenerateDistributedID.getPurchaseOrderNo(order.getAgencyCode(), order.getShopCode()));
    baseMapper.insert(order);

    int count = 1;
    for (PurchaseOrderDetail detail : details) {
      detail.setPurchaseOrderId(order.getId());
      detail.setDetailNo(order.getNo() + String.format("%03d", count++));
      // 乘经销商折扣 保存小数点后2位 四舍五入向上舍人
      detail.setDiscountAmount(detail.getAmount().multiply(order.getDiscount(), MathContext.DECIMAL128).setScale(2, BigDecimal.ROUND_HALF_UP));
      detail.setNewDiscountAmount(detail.getDiscountAmount());
      detail.setNewTotal(detail.getTotal());
    }

    // 批量插入
    purchaseOrderDetailService.insertBatch(details);

    // 订单地址信息
    AddressVo vo = purchaseOrderRequest.getAddressVo();
    PurchaseOrderConsignee consignee = new PurchaseOrderConsignee();
    consignee.setPurchaseOrderId(order.getId());
    consignee.setMobile(vo.getCellphone());
    consignee.setName(vo.getReceiverName());
    consignee.setAddr(vo.getAddress());
    consignee.setCity(vo.getCity());
    consignee.setProvince(vo.getProvince());
    consignee.setDistrict(vo.getDistrict());
    purchaseOrderConsigneeService.insert(consignee);

    return order;
  }

  @Override public Page<PurchaseOrderResponse> selectOrderPage(
      PurchaseOrder purchaseOrder, Page<PurchaseOrderResponse> page, PurchaseOrderListRequest request,boolean flag
  ) {
    List<PurchaseOrder> orders = null;
    //导出Excel
    if(flag){
      orders = baseMapper.selectOrderPage(purchaseOrder, null, null, request);
    }else{
      orders = baseMapper.selectOrderPage(purchaseOrder, page.getLimit(), page.getOffset(), request);
    }

    if (orders != null && orders.size() > 0) {
      Map<Long,String> userMap =  getUserInfoList();

      List<PurchaseOrderResponse> purchaseOrderVos = new ArrayList<>(orders.size());
      orders.forEach(it -> {
        PurchaseOrderResponse orderVo = new PurchaseOrderResponse();
        BeanUtils.copyProperties(it, orderVo);

        orderVo.setAgencyName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(it.getAgencyCode()));
        orderVo.setShopName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(it.getShopCode()));

        if(userMap != null && it.getCreatorId() != null){
          orderVo.setCreatorName(userMap.get(it.getCreatorId()));
        }

        List<PurchaseOrderDetail> sourceDetails = purchaseOrderDetailService.selectList(
            new EntityWrapper<>(
                new PurchaseOrderDetail(it.getId())));
        List<PurchaseOrderDetailResponse> targetDetailVos = new ArrayList<>(sourceDetails.size());
        sourceDetails.forEach(sourceDetail -> {
          PurchaseOrderDetailResponse targetDetailVo = new PurchaseOrderDetailResponse();
          BeanUtils.copyProperties(sourceDetail, targetDetailVo);

          targetDetailVos.add(targetDetailVo);
        });
        orderVo.setDetailVos(targetDetailVos);
        purchaseOrderVos.add(orderVo);
      });

      // FIXME 需要获取经销商信息 把经销商code和管理员ID换成名称
      page.setRecords(purchaseOrderVos);

      Integer total = baseMapper.selectOrderPageCount(purchaseOrder, request);

      page.setTotal(total);
    }
    return page;
  }

  @Override public List<PurchaseOrderDetailResponse> selectOrderDetail(Long orderId) {

    List<PurchaseOrderDetail> sourceDetails = purchaseOrderDetailService.selectList(
        new EntityWrapper<>(new PurchaseOrderDetail(orderId))
    );
    List<PurchaseOrderDetailResponse> targetDetailVos = new ArrayList<>(sourceDetails.size());

    sourceDetails.forEach(it -> {
      PurchaseOrderDetailResponse detailResponse = new PurchaseOrderDetailResponse();
      BeanUtils.copyProperties(it, detailResponse);
      targetDetailVos.add(detailResponse);
    });

    return targetDetailVos;
  }

  @Override public Page<PurchaseOrderResponse> selectOrderForErpPage(Page<PurchaseOrderResponse> page, ErpOrdersRequest request) {
    List<PurchaseOrder> orders = baseMapper.selectOrderForErpPage(page.getLimit(), page.getOffset(), request);

    if (orders.size() > 0) {
      HashMap<String, String> outerCodeMap = erpConfig.getAllAgencyOuterCodeMap();

      List<PurchaseOrderResponse> purchaseOrderVos = new ArrayList<>(orders.size());
      orders.forEach(it -> {
        PurchaseOrderResponse orderVo = new PurchaseOrderResponse();
        BeanUtils.copyProperties(it, orderVo);

        String outCodeMap = outerCodeMap.get(it.getAgencyCode());
        if (StringUtils.isBlank(outCodeMap)) {
          // 重新加载
          erpConfig.reloadAllAgencyAndShopNameAndSourceMap();
          orderVo.setOuterCode(outerCodeMap.get(it.getAgencyCode()));
        } else {
          orderVo.setOuterCode(outCodeMap);
        }

        // 设置门店来源ID 用于erp表示门店
        String sourceKey = it.getAgencyCode() + it.getShopCode();
        orderVo.setSourceId(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(sourceKey));

        if (StringUtils.isBlank(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(it.getAgencyCode())) ||
            StringUtils.isBlank(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(it.getShopCode()))) {

          // 重新加载
          erpConfig.reloadAllAgencyAndShopNameAndSourceMap();
        }

        // 设置经销商与门店名称
        orderVo.setAgencyName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(it.getAgencyCode()));
        orderVo.setShopName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(it.getShopCode()));



        List<PurchaseOrderDetail> sourceDetails = purchaseOrderDetailService.selectList(
            new EntityWrapper<>(
                new PurchaseOrderDetail(it.getId())));
        List<PurchaseOrderDetailResponse> targetDetailVos = new ArrayList<>(sourceDetails.size());
        sourceDetails.forEach(sourceDetail -> {
           if(!(sourceDetail.getFlag() != null && PurchaseOrderMaterialStatus.MATERIAL_DELETE.getCode().equals(sourceDetail.getFlag()))){
             String sku = sourceDetail.getSkuNo();
             BigDecimal discount = sourceDetail.getDiscountAmount();//折后价
             if(sku != null && sku.startsWith("TZ")){
               //表示组合商品 TZ00101
               //根据TZ00101从商品服务中查询商品信息
               ResponseData responseData = materialService.getskuBygroupNumber(sku);
               List<MaterialResponse> materialResponses = JsonTransformer
                   .getObjectMapper()
                   .convertValue(responseData.getData(), new TypeReference<List<MaterialResponse>>() {});
               if(materialResponses != null && !materialResponses.isEmpty()) {
                 BigDecimal total = new BigDecimal(0);
                 for (int m = 0;m<materialResponses.size();m++){
                   MaterialResponse  materialResponse = materialResponses.get(m);
                   BigDecimal amount = materialResponse.getAmount()==null?new BigDecimal(0):materialResponse.getAmount();
                   Integer qty = materialResponse.getStockQty()==null?0:materialResponse.getStockQty();
                   total = total.add(amount.multiply(new BigDecimal(qty)));
                 }

                 for (int n = 0;n<materialResponses.size();n++){
                   MaterialResponse  materialResponse = materialResponses.get(n);
                   PurchaseOrderDetailResponse detailVo = new PurchaseOrderDetailResponse();
                   BeanUtils.copyProperties(materialResponse, detailVo);
                   if(total.compareTo(new BigDecimal(0)) == 0){
                     detailVo.setAmount(new BigDecimal(0));  //单价
                   }else{
                     detailVo.setAmount(materialResponse.getAmount().divide(total,BigDecimal.ROUND_HALF_UP).multiply(sourceDetail.getAmount()));
                   }
                   detailVo.setTotal(materialResponse.getStockQty()); //数量
                   detailVo.setSkuNo(materialResponse.getSpu());
                   detailVo.setSkuSn(materialResponse.getSku());
                   detailVo.setDetailNo(sourceDetail.getDetailNo());
                   targetDetailVos.add(detailVo);
                 }
               }
             }else{
               //不是组合商品
               PurchaseOrderDetailResponse targetDetailVo = new PurchaseOrderDetailResponse();
               BeanUtils.copyProperties(sourceDetail, targetDetailVo);
               targetDetailVos.add(targetDetailVo);
             }
           }
        });

        orderVo.setDetailVos(targetDetailVos);

        purchaseOrderVos.add(orderVo);

        if (it.getConsignee() != null) {
          OrderConsigneeVo consigneeVo = new OrderConsigneeVo();
          BeanUtils.copyProperties(it.getConsignee(), consigneeVo);
          orderVo.setOrderConsignee(consigneeVo);
        }
      });

      // FIXME 需要获取经销商信息 把经销商code和管理员ID换成名称
      page.setRecords(purchaseOrderVos);

      Integer total = baseMapper.selectOrderForErpPageCount(request);

      page.setTotal(total);
    }

    logger().debug("返回给ERP的经销商采购订单数据：{}", JsonTransformer.toJson(page));

    return page;
  }

  @Override public Boolean paySuccessFully(PurchaseOrder purchaseOrder) {
    return baseMapper.paySuccessFully(purchaseOrder);
  }

  @Override public List<PurchaseOrder> selectWaitingPaymentList() {
    return baseMapper.selectWaitingPaymentList();
  }

  public Boolean existByNo(String no) {
    return (baseMapper.selectCount(new EntityWrapper<>(new PurchaseOrder(no))) > 0);
  }

  /**
   * 新增采购订单中的商品
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseData addMaterial(PurchaseAddMaterialRequest request) {

    //查找采购订单
    Map<String,Object> map = new HashMap<>();
    map.put("no",request.getOrderNo());
    List<PurchaseOrder> list =  selectByMap(map);
    if(list == null || list.isEmpty()){
      return ResponseData.build(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "该订单号不存在，不允许新增");
    }

    PurchaseOrder order = list.get(0);
    if(!PurchaseOrderStatus.WAIT_PAY.getCode().equals(order.getStatusId())){
      return ResponseData.build(ResponseBackCode.ERROR_CREATE_FAIL.getValue(), "该订单只有等待付款时，才能对商品进行新增");
    }


    PurchaseOrderDetail detail = new PurchaseOrderDetail();
    BeanUtils.copyProperties(request,detail);

    detail.setPurchaseOrderId(order.getId());

    //查找该订单中所有订单详情列表
    Map<String,Object> detailMap = new HashMap<>();
    detailMap.put("purchase_order_id",order.getId());
    List<PurchaseOrderDetail> detailList =  purchaseOrderDetailService.selectByMap(detailMap);
    detail.setDetailNo(order.getNo() + String.format("%03d", (detailList.size()+1)));
    detail.setFlag(PurchaseOrderMaterialStatus.MATERIAL_ADD.getCode());

    purchaseOrderDetailService.insert(detail);

    //更新采购金额和数量
    BigDecimal amount = detail.getNewDiscountAmount().multiply(new BigDecimal(detail.getNewTotal()));
    order.setAmount(order.getAmount().add(amount));
    order.setPayableAmount(order.getPayableAmount().add(amount));
    order.setUpdatedAt(LocalDateTime.now());
    updateById(order);

    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(),ResponseBackCode.SUCCESS.getMessage());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseData deleteMaterial(String orderNo) {
    Map<String,Object> detailMap = new HashMap<>();
    detailMap.put("detail_no",orderNo);
    List<PurchaseOrderDetail> detailList =  purchaseOrderDetailService.selectByMap(detailMap);
    if(detailList == null || detailList.isEmpty()){
      return ResponseData.build(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "该商品不存在，不允许删除");
    }
    PurchaseOrderDetail detail = detailList.get(0);

    //更新采购单金额和数量
    PurchaseOrder order = selectById(detail.getPurchaseOrderId());
    BigDecimal amount = detail.getNewDiscountAmount().multiply(new BigDecimal(detail.getNewTotal()));
    order.setAmount(order.getAmount().subtract(amount));
    order.setPayableAmount(order.getPayableAmount().subtract(amount));
    order.setUpdatedAt(LocalDateTime.now());
    updateById(order);

    //更新采购单详情的金额和数量
    if(!PurchaseOrderStatus.WAIT_PAY.getCode().equals(order.getStatusId())){
      return ResponseData.build(ResponseBackCode.ERROR_CREATE_FAIL.getValue(), "该订单只有等待付款时，才能对商品进行删除");
    }
    detail.setFlag(PurchaseOrderMaterialStatus.MATERIAL_DELETE.getCode());
    detail.setNewTotal(0);
    detail.setUpdatedAt(LocalDateTime.now());
    purchaseOrderDetailService.updateById(detail);
    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(),ResponseBackCode.SUCCESS.getMessage());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseData updateMaterial(String orderNo, PurchaseUpdateMaterialRequest request) {
    Map<String,Object> detailMap = new HashMap<>();
    detailMap.put("detail_no",orderNo);
    List<PurchaseOrderDetail> detailList =  purchaseOrderDetailService.selectByMap(detailMap);
    if(detailList == null || detailList.isEmpty()){
      return ResponseData.build(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "该商品不存在，不允许修改");
    }
    PurchaseOrderDetail detail = detailList.get(0);
    PurchaseOrder order = selectById(detail.getPurchaseOrderId());
    if(!PurchaseOrderStatus.WAIT_PAY.getCode().equals(order.getStatusId())){
      return ResponseData.build(ResponseBackCode.ERROR_CREATE_FAIL.getValue(), "该订单只有等待付款时，才能对商品进行修改");
    }

    //更新采购单金额和数量
    BigDecimal oldAmount = detail.getNewDiscountAmount().multiply(new BigDecimal(detail.getNewTotal()));
    BigDecimal newAmount = request.getNewDiscountAmount().multiply(new BigDecimal(request.getNewTotal()));
    BigDecimal amount = newAmount.subtract(oldAmount);
    order.setAmount(order.getAmount().add(amount));
    order.setPayableAmount(order.getPayableAmount().add(amount));
    order.setUpdatedAt(LocalDateTime.now());
    updateById(order);

    //更新采购单详情的金额和数量
    detail.setFlag(PurchaseOrderMaterialStatus.MATERIAL_EDIT.getCode());
    detail.setUpdatedAt(LocalDateTime.now());
    detail.setNewTotal(request.getNewTotal());
    detail.setNewDiscountAmount(request.getNewDiscountAmount());
    purchaseOrderDetailService.updateById(detail);
    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(),ResponseBackCode.SUCCESS.getMessage());
  }

    /**
   * 获取用户
   */
  private Map<Long,String> getUserInfoList() {
    ResponseData responseData =  salesUserService.getUserInfoList();
    List<IndexUserResponse> userResponseList = JsonTransformer
        .getObjectMapper()
        .convertValue(responseData.getData(), new TypeReference<List<IndexUserResponse>>() {});
    Map<Long,String> map = new HashMap<>();
    if(userResponseList != null && !userResponseList.isEmpty()){
      for (int i = 0,size = userResponseList.size(); i<size; i++){
        IndexUserResponse response = userResponseList.get(i);
        map.put(response.getId(),response.getName());
      }
    }
    return map;
  }
}
