package cn.sipin.sales.cloud.purchase.service.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import cn.sipin.sales.cloud.pojo.PurchaseOrder;
import cn.sipin.sales.cloud.pojo.PurchaseOrderDetail;
import cn.sipin.sales.cloud.request.ErpOrdersRequest;
import cn.sipin.sales.cloud.request.PurchaseAddMaterialRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderRequest;
import cn.sipin.sales.cloud.request.PurchaseUpdateMaterialRequest;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;
import cn.sipin.sales.cloud.request.PurchaseOrderListRequest;
import cn.siyue.platform.base.ResponseData;

/**
 * <p>
 * 采购单 服务类
 * </p>
 *
 * @author ${author}
 * @since 2018-06-19
 */
public interface PurchaseOrderServiceContract extends IService<PurchaseOrder> {


  PurchaseOrder create(PurchaseOrder order, List<PurchaseOrderDetail> details,PurchaseOrderRequest purchaseOrderRequest);

  Page<PurchaseOrderResponse> selectOrderPage(PurchaseOrder purchaseOrder, Page<PurchaseOrderResponse> orderVoPage, PurchaseOrderListRequest request,boolean flag);

  List<PurchaseOrderDetailResponse> selectOrderDetail(Long orderId);

  Page<PurchaseOrderResponse> selectOrderForErpPage(Page<PurchaseOrderResponse> orderVoPage, ErpOrdersRequest request);

  Boolean paySuccessFully(PurchaseOrder purchaseOrder);

  List<PurchaseOrder> selectWaitingPaymentList();

  ResponseData addMaterial(PurchaseAddMaterialRequest request);

  ResponseData deleteMaterial(String orderNo);

  ResponseData updateMaterial(String orderNo, PurchaseUpdateMaterialRequest request);
}
