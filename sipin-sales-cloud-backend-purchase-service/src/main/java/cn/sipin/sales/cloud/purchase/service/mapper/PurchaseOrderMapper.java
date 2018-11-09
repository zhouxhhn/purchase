package cn.sipin.sales.cloud.purchase.service.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import cn.sipin.sales.cloud.pojo.PurchaseOrder;
import cn.sipin.sales.cloud.request.ErpOrdersRequest;
import cn.sipin.sales.cloud.request.PurchaseOrderListRequest;

/**
 * <p>
 * 采购单 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2018-06-19
 */
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {

  List<PurchaseOrder> selectOrderPage(@Param("order") PurchaseOrder purchaseOrder, @Param("limit") Integer limit, @Param("offset") Integer offset, @Param("request")
      PurchaseOrderListRequest request);

  Integer selectOrderPageCount(@Param("order") PurchaseOrder purchaseOrder, @Param("request") PurchaseOrderListRequest request);

  List<PurchaseOrder> selectOrderForErpPage(@Param("limit") int limit, @Param("offset") int offset, @Param("request") ErpOrdersRequest request);

  Integer selectOrderForErpPageCount(@Param("request") ErpOrdersRequest request);

  Boolean paySuccessFully(@Param("purchaseOrder") PurchaseOrder purchaseOrder);

  List<PurchaseOrder> selectWaitingPaymentList();
}
