package cn.sipin.sales.cloud.purchase.service.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import cn.sipin.sales.cloud.constants.PurchaseOrderStatus;
import cn.sipin.sales.cloud.pojo.PurchaseOrder;
import cn.sipin.sales.cloud.pojo.PurchaseOrderExpress;
import cn.sipin.sales.cloud.purchase.service.mapper.PurchaseOrderExpressMapper;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderExpressServiceContract;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderServiceContract;
import cn.sipin.sales.cloud.request.ErpOrderExpressRequest;

/**
 * <p>
 * 采购订单快递物流表 服务实现类
 * </p>
 */
@Service
public class PurchaseOrderExpressService extends ServiceImpl<PurchaseOrderExpressMapper, PurchaseOrderExpress> implements PurchaseOrderExpressServiceContract {

  private PurchaseOrderServiceContract purchaseOrderService;

  @Autowired
  public PurchaseOrderExpressService(PurchaseOrderServiceContract purchaseOrderService) {
    this.purchaseOrderService = purchaseOrderService;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override public Boolean create(PurchaseOrder purchaseOrder, ErpOrderExpressRequest request) {
    if (purchaseOrder.getStatusId().compareTo(PurchaseOrderStatus.WAIT_DELIVERY.getCode()) >= 0 &&
        purchaseOrder.getStatusId().compareTo(PurchaseOrderStatus.PARTIAL_DELIVERED.getCode()) <= 0) {

      PurchaseOrderExpress express = new PurchaseOrderExpress();
      express.setOrderNo(request.getOrderNo());
      express.setExpressNo(request.getExpressNo());
      PurchaseOrderExpress tempExpress = this.baseMapper.selectOne(express);
      if (Objects.isNull(tempExpress) || Objects.isNull(tempExpress.getId())) {
        express.setExpressCompany(request.getExpressCompanyName());
        express.setExpressCompanyCode(request.getCompanyCode());
        this.insert(express);

        return true;
      }

      // 证明已有了记录 返回已创建
      return true;
    }

    return false;
  }
}
