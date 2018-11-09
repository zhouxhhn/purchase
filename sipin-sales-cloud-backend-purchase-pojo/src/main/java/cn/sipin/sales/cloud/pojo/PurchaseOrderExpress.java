package cn.sipin.sales.cloud.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购订单快递物流表
 * </p>
 *
 */
@Data
@Accessors(chain = true)
@TableName("purchase_order_express")
public class PurchaseOrderExpress extends Model<PurchaseOrderExpress> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 运单号
     */
    @TableField("express_no")
    private String expressNo;
    /**
     * K3物流公司名称
     */
    @TableField("express_company")
    private String expressCompany;
    /**
     * K3物流公司code
     */
    @TableField("express_company_code")
    private String expressCompanyCode;
    /**
     * 备注
     */
    private String note;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @Override protected Serializable pkVal() {
        return this.id;
    }

    public PurchaseOrderExpress() {
    }

    public PurchaseOrderExpress(String orderNo) {
        this.orderNo = orderNo;
    }

}
