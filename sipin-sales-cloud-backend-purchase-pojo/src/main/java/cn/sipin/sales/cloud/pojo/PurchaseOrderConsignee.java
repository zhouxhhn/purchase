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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 采购订单收货人
 * </p>
 *
 */
@TableName("purchase_order_consignee")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class PurchaseOrderConsignee extends Model<PurchaseOrderConsignee> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 采购订单
     */
    @TableField("purchase_order_id")
    private Long purchaseOrderId;
    /**
     * 收货人
     */
    private String name;
    /**
     * 收货人手机
     */
    private String mobile;
    /**
     * 收货人电话
     */
    private String phone;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String district;
    /**
     * 详细地址
     */
    private String addr;
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

    public PurchaseOrderConsignee(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    @Override protected Serializable pkVal() {
        return this.id;
    }
}
