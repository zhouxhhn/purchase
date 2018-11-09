/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.constants;

public enum PurchaseOrderMaterialStatus {

  MATERIAL_ADD(0, "新增"),

  MATERIAL_EDIT(1, "修改"),

  MATERIAL_DELETE(2, "删除");

  private Integer code;

  private String description;

  PurchaseOrderMaterialStatus(Integer code, String description) {
    this.code = code;
    this.description = description;
  }

  public static Boolean contains(Integer code) {
    if (code == null) {
      return false;
    }
    for (PurchaseOrderMaterialStatus orderStatus : PurchaseOrderMaterialStatus.values()) {
      if (orderStatus.code.equals(code)) {
        return true;
      }
    }

    return false;
  }

  public static PurchaseOrderMaterialStatus getEnum(Integer code) {
    if (code == null) {
      return null;
    }
    for (PurchaseOrderMaterialStatus orderStatus : PurchaseOrderMaterialStatus.values()) {
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
