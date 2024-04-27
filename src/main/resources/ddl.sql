CREATE DATABASE `ecommerce`;
GRANT ALL PRIVILEGES ON `ecommerce`.* TO 'ecommerce'@'localhost' IDENTIFIED BY 'ecommerce';
FLUSH PRIVILEGES;

# USE `ecommerce`;

DROP TABLE IF EXISTS `ecommerce`.`tb_user`;
CREATE TABLE `ecommerce`.`tb_user` (
   `user_id`    BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '유저 ID',
   `point`      BIGINT(20) NOT NULL COMMENT '포인트',
   PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_orders`;
CREATE TABLE `tb_orders` (
     `order_id`     BIGINT      NOT NULL AUTO_INCREMENT COMMENT '주문 ID',
     `user_id`      BIGINT      DEFAULT NULL COMMENT '유저 ID',
     `order_status` VARCHAR(15) DEFAULT NULL COMMENT '주문 상태',
     `order_price`  BIGINT      DEFAULT NULL COMMENT '총 주문 금액',
     `ordered_at`  DATETIME(6) DEFAULT NULL COMMENT '주문 일시',
     PRIMARY KEY (`order_id`),
     KEY `idx_user_id` (`user_id`),
     KEY `idx_create_date` (`ordered_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_order_line`;
CREATE TABLE `tb_order_line` (
     `order_line_id`    BIGINT NOT NULL AUTO_INCREMENT COMMENT '주문 상품 ID',
     `order_id`         BIGINT DEFAULT NULL COMMENT '주문 ID',
     `item_id`          BIGINT DEFAULT NULL COMMENT '상품 ID',
     `item_price`       BIGINT NOT NULL COMMENT '상품 가격',
     `order_quantity`   BIGINT NOT NULL COMMENT '주문 수량',
     `status`           VARCHAR(15) NOT NULL COMMENT '주문 상품 상태',
     PRIMARY KEY (`order_line_id`),
     KEY `idx_order_id` (`order_id`),
     KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_item`;
CREATE TABLE `tb_item` (
   `item_id`        BIGINT NOT NULL AUTO_INCREMENT COMMENT '상품 ID',
   `item_name`      VARCHAR(50) NOT NULL COMMENT '상품명',
   `item_price`     BIGINT NOT NULL COMMENT '상품 가격',
   `stock_quantity` BIGINT NOT NULL COMMENT '재고 수량',
   PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_cart`;
CREATE TABLE `tb_cart` (
   `cart_id`        BIGINT NOT NULL AUTO_INCREMENT COMMENT '장바구니 ID',
   `user_id`        bigint(20) DEFAULT NULL COMMENT '유저 ID',
   `item_id`        bigint(20) DEFAULT NULL COMMENT '상품 ID',
   `quantity`       bigint(20) DEFAULT NULL COMMENT '상품 수량',
   `create_date`    datetime(6) DEFAULT NULL COMMENT '장바구니 생성 일시',
   PRIMARY KEY (`cart_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
