package cn.sipin.sales.cloud.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 采购订单快递
 * </p>
 *
 */
@Data
@ApiModel(value = "采购订单物流快递信息(PurchaseOrderExpressResponse)")
public class PurchaseOrderExpressResponse  {
    /**
     * 运单号
     */
    @ApiModelProperty(value = "运单号")
    private String expressNo;
    /**
     * K3物流公司名称
     */
    @ApiModelProperty(value = "K3物流公司名称")
    private String expressCompany;
    /**
     * K3物流公司code
     */
    @ApiModelProperty(value = "K3物流公司code")
    private String expressCompanyCode;

    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;


}
