CREATE SCHEMA IF NOT EXISTS `demo`;
USE `demo`;

CREATE TABLE todo (
    id INT NOT NULL AUTO_INCREMENT COMMENT '식별키',
    message VARCHAR(255) NOT NULL COMMENT '메시지',
    is_done TINYINT(1) NOT NULL COMMENT '완료 여부',
    created_at DATETIME(6) NOT NULL COMMENT '생성일',
    updated_at DATETIME(6) NOT NULL COMMENT '수정일',
    PRIMARY KEY (`id`)
) COMMENT '할일'
