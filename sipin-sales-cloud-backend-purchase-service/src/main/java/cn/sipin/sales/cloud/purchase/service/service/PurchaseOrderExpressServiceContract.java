package cn.sipin.sales.cloud.purchase.service.service;

import com.baomidou.mybatisplus.service.IService;

import cn.sipin.sales.cloud.pojo.PurchaseOrder;
import cn.sipin.sales.cloud.pojo.PurchaseOrderExpress;
import cn.sipin.sales.cloud.request.ErpOrderExpressRequest;

/**
 * <p>
 * 采购订单快递物流表 服务类
 * </p>
 *

 */
public interface PurchaseOrderExpressServiceContract extends IService<PurchaseOrderExpress> {

  Boolean create(PurchaseOrder purchaseOrder, ErpOrderExpressRequest request);
}
