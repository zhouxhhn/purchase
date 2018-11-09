package cn.sipin.sales.cloud.purchase.service.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import cn.sipin.sales.cloud.pojo.PurchaseOrderDetail;
import cn.sipin.sales.cloud.purchase.service.mapper.PurchaseOrderDetailMapper;
import cn.sipin.sales.cloud.purchase.service.service.PurchaseOrderDetailServiceContract;

/**
 * <p>
 * 采购单明细 服务实现类
 * </p>
 *
 */
@Service
public class PurchaseOrderDetailService extends ServiceImpl<PurchaseOrderDetailMapper, PurchaseOrderDetail> implements PurchaseOrderDetailServiceContract {

}
