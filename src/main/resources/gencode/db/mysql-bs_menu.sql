-- Date:2019-4-30 14:55:28
-- author:BearBear

SET FOREIGN_KEY_CHECKS=0; 

-- DROP TABLE IF EXISTS `bs_menu`;
CREATE TABLE `bs_menu` (
  `id` VARCHAR(100) NOT NULL  COMMENT 'id',
  `menu_name` VARCHAR(100) DEFAULT NULL  COMMENT '菜单名称',
  `pid` VARCHAR(100) DEFAULT NULL  COMMENT 'pid',
  `icon` VARCHAR(1024) DEFAULT NULL  COMMENT 'icon',
  `icon_flag` INT(20) DEFAULT NULL  COMMENT 'iconFlag',
  `power_id` VARCHAR(100) DEFAULT NULL COMMENT '权限表',
  KEY `rs_power_menu` (`power_id`) USING BTREE,
  CONSTRAINT `fk_rs_power_menu` FOREIGN KEY (`power_id`) REFERENCES `bs_power` (`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';