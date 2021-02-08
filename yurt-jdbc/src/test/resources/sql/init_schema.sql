CREATE DATABASE IF NOT EXISTS testdb ;
USE testdb;

DROP TABLE IF EXISTS sign_product;
CREATE TABLE sign_product (
    id         BIGINT PRIMARY KEY      NOT NULL AUTO_INCREMENT,
    code       VARCHAR(32) DEFAULT ''  NOT NULL,
    full_name  VARCHAR(128) DEFAULT '' NOT NULL,
    inventory  INT DEFAULT 0           NOT NULL,
    price      DOUBLE(12, 3) DEFAULT 0 NOT NULL,
    is_deleted TINYINT(1)    DEFAULT 0 NOT NULL,
    gmt_deleted TIMESTAMP                       DEFAULT '2000-01-01',
    gmt_created TIMESTAMP                        DEFAULT current_timestamp,
    gmt_updated TIMESTAMP                        DEFAULT current_timestamp
    ON UPDATE current_timestamp
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;


INSERT INTO sign_product  (code, full_name, inventory, price) VALUES ('893341', '远见产品3号', 12, 98.99);
INSERT INTO sign_product  (code, full_name, inventory, price) VALUES ('213324', '这是测试产品', 543, 189);
INSERT INTO sign_product  (code, full_name, inventory, price) VALUES ('123873', '正式环境产品53', 76, 32.88);
INSERT INTO sign_product  (code, full_name, inventory, price) VALUES ('678567', '招商银行限量', 9765, 18.89);

