/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.constants;

public enum PurchaseOrderStatus {

  WAIT_PAY(0, "等待付款"),

  WAIT_FINAL_PAY(1, "等待尾款"),

  PAY_SUCCESSFULLY(2, "付款成功"),

  WAIT_DELIVERY(3, "等待发货"),

  DELIVERED(4, "已发货"),

  FINISHED(5, "已完成"),

  PARTIAL_DELIVERED(6, "部分发货"),

  FROZEN(100, "已冻结"),

  WAIT_RETURN_AUDIT(200, "退货待审"),

  WAIT_RETURN_PRODUCT(201, "等待退货"),

  PRODUCT_RETURNED(202, "已退货"),

  WAIT_RETURN_MONEY(203, "退款中"),

  CANCELED(300, "已取消");

  private Integer code;

  private String description;

  PurchaseOrderStatus(Integer code, String description) {
    this.code = code;
    this.description = description;
  }

  public static Boolean contains(Integer code) {
    if (code == null) {
      return false;
    }
    for (PurchaseOrderStatus orderStatus : PurchaseOrderStatus.values()) {
      if (orderStatus.code.equals(code)) {
        return true;
      }
    }

    return false;
  }

  public static PurchaseOrderStatus getEnum(Integer code) {
    if (code == null) {
      return null;
    }
    for (PurchaseOrderStatus orderStatus : PurchaseOrderStatus.values()) {
      if (orderStatus.code.equals(code)) {
        return orderStatus;
      }
    }

    return null;
  }

  public Integer getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

}
