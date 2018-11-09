/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.purchase.client.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import cn.sipin.sales.cloud.constants.PurchaseOrderStatus;
import cn.sipin.sales.cloud.response.PurchaseOrderDetailResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderExpressResponse;
import cn.sipin.sales.cloud.response.PurchaseOrderResponse;

public class ExportPurchaseOrderExcelUtils {

  public static void export(HttpServletResponse res, List<PurchaseOrderResponse> ordersList) throws Exception{
    String[] orderTitles = { "采购订单号", "门店", "经销商", "下单时间","发货时间", "订单状态", "操作员","合计金额","应付金额", "已付金额", "备注"};
    String[] orderDetailTitles = {"商品交易号", "商品名称", "属性规格", "数量", "采购价","小计", "物流公司","物流单号"};

    ServletOutputStream out=res.getOutputStream();
    String fileName=new String((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), "UTF-8");
    res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    res.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");

    // 1.创建一个workbook，对应一个Excel文件
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 2.在webbook中添加一个sheet,对应Excel文件中的sheet
    HSSFSheet hssfSheet = workbook.createSheet("采购订单");
    // 3.在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
    HSSFRow hssfRow = hssfSheet.createRow(0);
    // 4.创建单元格，并设置值表头 设置表头居中
    HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
    //居中样式
    hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

    HSSFCell hssfCell = null;
    int length = orderTitles.length;
    for (int i = 0; i < length; i++) {
      hssfCell = hssfRow.createCell(i);//列索引从0开始
      hssfCell.setCellValue(orderTitles[i]);//列名1
      hssfCell.setCellStyle(hssfCellStyle);//列居中显示
    }

    for (int i = 0; i < orderDetailTitles.length; i++) {
      hssfCell = hssfRow.createCell(length+i);
      hssfCell.setCellValue(orderDetailTitles[i]);//列名1
      hssfCell.setCellStyle(hssfCellStyle);//列居中显示
    }


    // 5.写入实体数据
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    int total = 1;

    if (ordersList != null && ordersList.size() > 0) {
      for (int i = 0; i < ordersList.size(); i++) {
        hssfRow = hssfSheet.createRow(total);
        PurchaseOrderResponse order = ordersList.get(i);


        //采购订单号
        String no = "";
        if (order.getNo() != null) {
          no = order.getNo() + "";
        }
        hssfRow.createCell(0).setCellValue(no);

        //门店
        String shopName = "";
        if (order.getShopName() != null) {
          shopName = order.getShopName();
        }
        hssfRow.createCell(1).setCellValue(shopName);

        //经销商
        String agencyName = "";
        if (order.getAgencyName() != null) {
          agencyName = order.getAgencyName();
        }
        hssfRow.createCell(2).setCellValue(agencyName);

        //下单时间
        String createdAt = "";
        if (order.getCreatedAt() != null) {
          createdAt = order.getCreatedAt().format(formatter);
        }
        hssfRow.createCell(3).setCellValue(createdAt);


        String expressCompany = "";
        String expressNo = "";
        LocalDateTime sendTime = null;
        if(order.getOrderExpressList() != null && !order.getOrderExpressList().isEmpty()){
          for (int m = 0,size = order.getOrderExpressList().size(); m < size; m++) {
            PurchaseOrderExpressResponse expressResponse = order.getOrderExpressList().get(m);
            expressCompany+=expressResponse.getExpressCompany();
            expressNo+=expressResponse.getExpressNo();
            sendTime = expressResponse.getCreatedAt();
          }
        }

        //发货时间
        String sendTimeAt = "";
        if(sendTime != null){
          sendTimeAt = sendTime.format(formatter);
        }
        hssfRow.createCell(4).setCellValue(sendTimeAt);

        //订单状态
        String statusName = "";
        if (order.getStatusId() != null) {
          statusName = PurchaseOrderStatus.getEnum(order.getStatusId()).getDescription();
        }
        hssfRow.createCell(5).setCellValue(statusName);

        //操作员
        String createrName = "";
        if (order.getCreatorName() != null) {
          createrName = order.getCreatorName();
        }
        hssfRow.createCell(6).setCellValue(createrName);

        //合计金额
        BigDecimal amount = new BigDecimal(0);
        if (order.getAmount() != null) {
          amount = order.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(7).setCellValue(amount.toString());

        //应付金额
        BigDecimal payableAmount = new BigDecimal(0);
        if (order.getPayableAmount() != null) {
          payableAmount = order.getPayableAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(8).setCellValue(payableAmount.toString());


        //已付金额
        BigDecimal paidAmount = new BigDecimal(0);
        if (order.getPaidAmount() != null && order.getPayableAmount() != null) {
          paidAmount = order.getPaidAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(9).setCellValue(paidAmount.toString());

        //备注
        String  note = "";
        if (order.getNote() != null) {
          note = order.getNote();
        }

        hssfRow.createCell(10).setCellValue(note);


        //订单金额
        if(order.getDetailVos() != null && !order.getDetailVos().isEmpty()){
          List<PurchaseOrderDetailResponse> detailResponses = order.getDetailVos();
          for (int j = 0,size = detailResponses.size(); j < size; j++) {
            PurchaseOrderDetailResponse detailResponse = detailResponses.get(j);
            if(j != 0){
              hssfRow = hssfSheet.createRow(total);
            }

            //商品交易号
            String  detailNo = "";
            if (detailResponse.getDetailNo() != null) {
              detailNo = detailResponse.getDetailNo();
            }
            hssfRow.createCell(11).setCellValue(detailNo);

            //商品名称
            String  name = "";
            if (detailResponse.getName() != null) {
              name = detailResponse.getName();
            }
            hssfRow.createCell(12).setCellValue(name);

            //属性规格
            String  specification = "规格：";
            if (detailResponse.getSpecification() != null) {
              specification += detailResponse.getSpecification();
            }
            String color = "颜色：";
            if (detailResponse.getColor() != null) {
              color += detailResponse.getColor();
            }

            String texture = "材质：";
            if (detailResponse.getTexture() != null) {
              texture += detailResponse.getTexture();
            }
            hssfRow.createCell(13).setCellValue(specification+" | "+color+" | "+texture);

            //商品数量
            Integer  newTotal = 0;
            if (detailResponse.getNewTotal() != null) {
              newTotal = detailResponse.getNewTotal();
            }
            hssfRow.createCell(14).setCellValue(newTotal);

            //采购价
            BigDecimal  newDiscountAmount = new BigDecimal(0);
            if (detailResponse.getNewDiscountAmount() != null) {
              newDiscountAmount = detailResponse.getNewDiscountAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            hssfRow.createCell(15).setCellValue(newDiscountAmount.toString());

            //小计
            BigDecimal  subtotal = newDiscountAmount.multiply(new BigDecimal(newTotal)).setScale(2, BigDecimal.ROUND_HALF_UP);
            hssfRow.createCell(16).setCellValue(subtotal.toString());

            //物流公司
            hssfRow.createCell(17).setCellValue(expressCompany);

            //物流单号
            hssfRow.createCell(18).setCellValue(expressNo);
            total++;
          }
        }
      }
    }
    // 7.将文件输出到客户端浏览器
    workbook.write(out);
    out.flush();
    out.close();

  }
}
