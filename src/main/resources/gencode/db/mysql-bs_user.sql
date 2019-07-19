-- Date:2019-4-30 14:55:28
-- author:BearBear

SET FOREIGN_KEY_CHECKS=0; 

-- DROP TABLE IF EXISTS `bs_user`;
CREATE TABLE `bs_user` (
  `id` VARCHAR(100) NOT NULL  COMMENT 'id',
  `name` VARCHAR(100) DEFAULT NULL  COMMENT '名字',
  `login_name` VARCHAR(500) DEFAULT NULL  COMMENT '账号',
  `login_psw` VARCHAR(500) DEFAULT NULL  COMMENT '密码',
  `state` INT(20) DEFAULT NULL  COMMENT '人员状态',
  `phone` VARCHAR(500) DEFAULT NULL  COMMENT '电话号码',
  `unit_id` VARCHAR(100) DEFAULT NULL COMMENT '单位表',
  KEY `rs_unit_user` (`unit_id`) USING BTREE,
  CONSTRAINT `fk_rs_unit_user` FOREIGN KEY (`unit_id`) REFERENCES `bs_unit` (`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';