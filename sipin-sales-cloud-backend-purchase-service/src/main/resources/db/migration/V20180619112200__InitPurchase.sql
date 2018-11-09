CREATE TABLE `purchase_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `no` varchar(45) NOT NULL COMMENT '采购单号',
  `status_id` int(11) NOT NULL DEFAULT '0' COMMENT '状态ID',
  `amount` decimal(12,2) DEFAULT NULL COMMENT '订单金额',
  `payable_amount` decimal(12,2) DEFAULT NULL COMMENT '应付金额',
  `paid_amount` decimal(12,2) DEFAULT NULL COMMENT '已付金额',
  `agency_code` varchar(64) NOT NULL COMMENT '经销商Code',
  `shop_code` varchar(64) NOT NULL COMMENT '店铺Code',
  `discount` decimal(12,2) DEFAULT NULL COMMENT '结算折扣',
  `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
  `auditor_id` bigint(20) DEFAULT NULL COMMENT '审核者ID',
  `audited_at` datetime DEFAULT NULL COMMENT '审核时间',
  `note` text COMMENT '备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '软删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `purchase_order_index_no` (`no`),
  KEY `purchase_order_index_agency_shop_code` (`agency_code`,`shop_code`)
) COMMENT='采购单';

CREATE TABLE `purchase_order_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `purchase_order_id` bigint(20) NOT NULL COMMENT '采购单ID',
  `detail_no` varchar(45) NOT NULL COMMENT '订单商品交易号',
  `sku_sn` varchar(50) NOT NULL DEFAULT '' COMMENT 'SKU',
  `sku_no` varchar(50) NOT NULL DEFAULT '' COMMENT '产品SKU编号',
  `name` varchar(150) NOT NULL DEFAULT '' COMMENT '商品名称',
  `specification` varchar(150) NOT NULL DEFAULT '' COMMENT '物料规格',
  `texture` varchar(150) NOT NULL DEFAULT '' COMMENT '物料材质',
  `color` varchar(50) NOT NULL DEFAULT '' COMMENT '物料颜色',
  `img_path` varchar(150) DEFAULT NULL COMMENT '图片地址',
  `total` int(11) DEFAULT NULL COMMENT '采购数',
  `stock_in_total` int(11) DEFAULT NULL COMMENT '已入库数',
  `amount` decimal(12,2) DEFAULT NULL COMMENT '售价',
  `discount_amount` decimal(12,2) DEFAULT NULL COMMENT '折后价',
  `note` text  DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `purchase_order_detail_unique_detail_no` (`detail_no`),
  KEY `purchase_order_detail_index_purchase_order_id` (`purchase_order_id`),
  KEY `purchase_order_detail_index_sku_no` (`sku_no`)
) COMMENT='采购单明细';

CREATE TABLE `purchase_order_consignee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `purchase_order_id` bigint(20) NOT NULL COMMENT '采购订单',
  `name` varchar(64) NOT NULL COMMENT '收货人',
  `mobile` varchar(32) NOT NULL COMMENT '收货人手机',
  `phone` varchar(32) DEFAULT NULL COMMENT '收货人电话',
  `province` varchar(50) NOT NULL COMMENT '省',
  `city` varchar(50) NOT NULL COMMENT '市',
  `district` varchar(50) NOT NULL COMMENT '区',
  `addr` varchar(128) NOT NULL COMMENT '详细地址',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `purchase_order_consignee_index_purchase_order_id` (`purchase_order_id`)
) COMMENT='采购订单收货人';

CREATE TABLE `purchase_order_express` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(45) NOT NULL COMMENT '订单号',
  `express_no` varchar(128) NOT NULL COMMENT '运单号',
  `express_company` varchar(64) DEFAULT NULL COMMENT 'K3物流公司名称',
  `express_company_code` varchar(25) DEFAULT NULL COMMENT 'K3物流公司code',
  `note` text NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `purchase_order_express_unique_order_express_no` (`order_no`,`express_no`)
) COMMENT='采购订单快递物流表';