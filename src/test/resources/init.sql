-- CREATE DATABASE IF NOT EXISTS `jinius`;
-- -- GRANT ALL PRIVILEGES ON `jinius`.* TO 'test'@'%' IDENTIFIED BY 'test';
-- -- FLUSH PRIVILEGES;
-- -- USE `jinius`;

DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
   `user_id`    INT NOT NULL AUTO_INCREMENT  COMMENT '유저 ID',
   `user_name`  VARCHAR(20) NOT NULL COMMENT '유저 이름',
   `point`      BIGINT(20) NOT NULL COMMENT '포인트',
   `version`    INT NOT NULL COMMENT '버전',
   PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_orders`;
CREATE TABLE `tb_orders` (
     `order_id`     INT      NOT NULL AUTO_INCREMENT COMMENT '주문 ID',
     `user_id`      INT      DEFAULT NULL COMMENT '유저 ID',
     `status`       VARCHAR(20) DEFAULT NULL COMMENT '주문 상태',
     `total_price`  BIGINT      DEFAULT NULL COMMENT '총 주문 금액',
     `created_at`   DATETIME DEFAULT CURRENT_TIMESTAMP not null COMMENT '주문 일시',
     `updated_at`   DATETIME DEFAULT CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP COMMENT '수정 일시',
     PRIMARY KEY (`order_id`),
     KEY `idx_user_id` (`user_id`),
     KEY `idx_create_date` (`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_order_item`;
CREATE TABLE `tb_order_item` (
     `order_item_id`    INT NOT NULL AUTO_INCREMENT COMMENT '주문 상품 ID',
     `order_id`         INT DEFAULT NULL COMMENT '주문 ID',
     `product_id`       INT DEFAULT NULL COMMENT '상품 ID',
     `product_price`    BIGINT NOT NULL COMMENT '상품 가격',
     `quantity`         INT NOT NULL COMMENT '주문 수량',
     `status`           VARCHAR(20) NOT NULL COMMENT '주문 상품 상태',
     `created_at`       DATETIME DEFAULT CURRENT_TIMESTAMP not null COMMENT '주문 일시',
     `updated_at`       DATETIME DEFAULT CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP COMMENT '수정 일시',
     PRIMARY KEY (`order_item_id`),
     KEY `idx_order_id` (`order_id`),
     KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_product`;
CREATE TABLE `tb_product` (
   `product_id`        INT NOT NULL AUTO_INCREMENT COMMENT '상품 ID',
   `product_name`      VARCHAR(50) NOT NULL COMMENT '상품명',
   `product_price`     BIGINT NOT NULL COMMENT '상품 가격',
   `stock_quantity`    INT NOT NULL COMMENT '재고 수량',
   PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_payment`;
CREATE TABLE `tb_payment` (
    `payment_id`        INT NOT NULL AUTO_INCREMENT COMMENT '결제 ID',
    `order_id`          INT NOT NULL COMMENT '주문 ID',
    `user_id`           INT NOT NULL COMMENT '유저 ID',
    `type`              VARCHAR(20) NOT NULL COMMENT '결제 유형',
    `amount`            BIGINT NOT NULL COMMENT '결제 금액',
    `point`             BIGINT NOT NULL COMMENT '포인트',
    `status`            VARCHAR(20) NOT NULL COMMENT '결제 상태',
    `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP not null COMMENT '주문 일시',
    `updated_at`        DATETIME DEFAULT CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP COMMENT '수정 일시',
    PRIMARY KEY (`payment_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_date` (`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_cart`;
CREATE TABLE `tb_cart` (
   `cart_id`        INT NOT NULL AUTO_INCREMENT COMMENT '장바구니 ID',
   `user_id`        INT DEFAULT NULL COMMENT '유저 ID',
   `item_id`        INT DEFAULT NULL COMMENT '상품 ID',
   `quantity`       INT DEFAULT NULL COMMENT '상품 수량',
   `created_at`      DATETIME DEFAULT CURRENT_TIMESTAMP not null COMMENT '장바구니 생성 일시',
   PRIMARY KEY (`cart_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO tb_user(USER_NAME, POINT, VERSION) VALUES ( '관리자', 10000, 1 );
INSERT INTO tb_user(USER_NAME, POINT, VERSION) VALUES ( '관리자', 20000, 1 );

INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('바람막이', 49900, 2000);
INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('청자켓', 59900, 30);
INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('아노락', 40000, 30);
INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('블루종', 39900, 30);
INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('후드티', 50000, 30);
INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('반팔티', 20000, 30);
INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('슬랙스', 45000, 30);
INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('카고', 33000, 30);
INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('조거팬츠', 48000, 30);
INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('셋업', 100000, 30);
INSERT INTO tb_product(PRODUCT_NAME, PRODUCT_PRICE, STOCK_QUANTITY) VALUES ('양말', 3000, 100);