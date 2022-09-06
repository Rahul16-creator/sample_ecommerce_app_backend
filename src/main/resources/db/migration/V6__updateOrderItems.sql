ALTER TABLE `order_details` RENAME TO `order_items`;
ALTER TABLE `order_items` MODIFY total_price DECIMAL (10,2);