package cn.sipin.sales.cloud.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 采购单明细
 * </p>
 *
 */
@TableName("purchase_order_detail")
@Data
@EqualsAndHashCode(callSuper = false)
public class PurchaseOrderDetail extends Model<PurchaseOrderDetail> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 采购单ID
     */
    @TableField("purchase_order_id")
    private Long purchaseOrderId;

    @TableField("detail_no")
    private String detailNo;
    /**
     * SKU
     */
    @TableField("sku_sn")
    private String skuSn;
    /**
     * 产品SKU编号
     */
    private String skuNo;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 物料规格
     */
    private String specification;
    /**
     * 物料材质
     */
    private String texture;
    /**
     * 物料颜色
     */
    private String color;
    /**
     * 图片地址
     */
    @TableField("img_path")
    private String imgPath;
    /**
     * 采购数
     */
    private Integer total;
    /**
     * 已入库数
     */
    @TableField("stock_in_total")
    private Integer stockInTotal;
    /**
     * 售价
     */
    private BigDecimal amount;

    /**
     * 折后价
     */
    @TableField("discount_amount")
    private BigDecimal discountAmount;

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

    /**
     * 现采购价
     */
    @TableField("new_discount_amount")
    private BigDecimal newDiscountAmount;

    /**
     *现采购数量
     */
    @TableField("new_total")
    private Integer newTotal;

    /**
     * 修改标志
     */
    private Integer flag;

    public PurchaseOrderDetail() {
    }

    public PurchaseOrderDetail(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    @Override protected Serializable pkVal() {
        return this.id;
    }
}
