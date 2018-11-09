ALTER TABLE purchase_order_detail ADD  `new_discount_amount` decimal(12,2)  COMMENT '现采购价';
ALTER TABLE purchase_order_detail ADD  `new_total` int(11)  COMMENT '现采购数量';
ALTER TABLE purchase_order_detail ADD  `flag` int(11)  COMMENT '修改标志';

update purchase_order_detail set new_discount_amount = discount_amount;
update purchase_order_detail set new_total = total;
