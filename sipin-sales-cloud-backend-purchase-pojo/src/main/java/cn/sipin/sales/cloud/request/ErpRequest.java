/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.request;

import java.util.Map;

public abstract class ErpRequest {

  public abstract Map<String, String> toParams();

}
