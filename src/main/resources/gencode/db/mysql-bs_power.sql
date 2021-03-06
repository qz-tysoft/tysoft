﻿-- Date:2019-4-30 14:55:28
-- author:BearBear

SET FOREIGN_KEY_CHECKS=0; 

-- DROP TABLE IF EXISTS `bs_power`;
CREATE TABLE `bs_power` (
  `id` VARCHAR(100) NOT NULL  COMMENT 'id',
  `power_name` VARCHAR(500) DEFAULT NULL  COMMENT '权限名称',
  `url` VARCHAR(500) DEFAULT NULL  COMMENT '地址',
  `menu_name` VARCHAR(100) DEFAULT NULL  COMMENT '菜单名称',
  `pid` VARCHAR(100) DEFAULT NULL  COMMENT 'pid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';