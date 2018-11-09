/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.purchase.service.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import cn.sipin.sales.cloud.constants.PurchaseOrderStatus;
import cn.sipin.sales.cloud.pojo.PurchaseOrder;
import cn.sipin.sales.cloud.purchase.service.contract.Loggable;
import cn.sipin.sales.cloud.purchase.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderServiceContract;

public class PaymentCompensationJob implements Job, Loggable {


  @Autowired
  private SalesUserServiceImpl salesUserService;
  @Autowired
  private PurchaseOrderServiceContract purchaseOrderService;


  @Override public void execute(JobExecutionContext context) throws JobExecutionException {
    logger().info("采购订单支付补偿轮询开始---");

    List<PurchaseOrder> purchaseOrderList = purchaseOrderService.selectWaitingPaymentList();
    purchaseOrderList.forEach(it -> {
      Boolean isPaid = salesUserService.isPaidByOrderNo(it.getNo());
      if (isPaid) {
        PurchaseOrder tempPurchaseOrder = new PurchaseOrder();
        tempPurchaseOrder.setId(it.getId());
        tempPurchaseOrder.setAuditorId(it.getCreatorId());
        tempPurchaseOrder.setAuditedAt(LocalDateTime.now());
        tempPurchaseOrder.setStatusId(PurchaseOrderStatus.PAY_SUCCESSFULLY.getCode());
        // TODO 以后有优惠信息 要改
        tempPurchaseOrder.setPaidAmount(it.getPayableAmount());
        purchaseOrderService.paySuccessFully(tempPurchaseOrder);
      }
    });
    logger().info("采购订单支付补偿轮询结束---");
  }
}
