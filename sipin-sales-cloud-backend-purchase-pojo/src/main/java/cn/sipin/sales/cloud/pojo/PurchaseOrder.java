package cn.sipin.sales.cloud.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * <p>
 * 采购单
 * </p>
 *
 */
@TableName("purchase_order")
@Data
public class PurchaseOrder extends Model<PurchaseOrder> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 采购单号
     */
    private String no;
    /**
     * 状态ID
     */
    @TableField("status_id")
    private Integer statusId;
    /**
     * 订单金额
     */
    private BigDecimal amount;
    /**
     * 应付金额
     */
    @TableField("payable_amount")
    private BigDecimal payableAmount;

    /**
     * 已付金额
     */
    @TableField("paid_amount")
    private BigDecimal paidAmount;

    /**
     * 经销商code
     */
    @TableField("agency_code")
    private String agencyCode;
    /**
     * 店铺code
     */
    @TableField("shop_code")
    private String shopCode;

    /**
     * 经销商折扣
     */
    private BigDecimal discount;



    /**
     * 创建者Id
     */
    @TableField("creator_id")
    private Long creatorId;
    /**
     * 审核者Id
     */
    @TableField("auditor_id")
    private Long auditorId;
    /**
     * 审核时间
     */
    @TableField("audited_at")
    private LocalDateTime auditedAt;
    /**
     * 备注
     */
    private String note;
    /**
     * 软删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
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


    @TableField(exist = false)
    private PurchaseOrderConsignee consignee;

    public PurchaseOrder() {
    }

    @Override protected Serializable pkVal() {
        return this.id;
    }

    public PurchaseOrder(String no) {
        this.no = no;
    }

    public PurchaseOrder(String agencyCode, String shopCode) {
        this.agencyCode = agencyCode;
        this.shopCode = shopCode;
    }

    public PurchaseOrder(String no, String agencyCode, String shopCode, Integer statusId) {
        this.no = no;
        this.agencyCode = agencyCode;
        this.shopCode = shopCode;
        this.statusId = statusId;
    }


}
