-- 请另存为UTF-8编码格式后再用mysql工具导入运行该文件（建议用EditPlus打开另存为UTF-8编码）
-- Date:2019-4-30 14:55:28
-- author:BearBear

SET FOREIGN_KEY_CHECKS=0; 

DROP TABLE IF EXISTS ss_user_role;

CREATE TABLE ss_user_role (
  `user_id` VARCHAR(100) NOT NULL COMMENT '用户表',
  KEY `idx_user_ss_user_role` (`user_id`) USING BTREE,
  CONSTRAINT `fk_user_ss_user_role` FOREIGN KEY (`user_id`) REFERENCES `bs_user` (`id`),
  `role_id` VARCHAR(100) NOT NULL COMMENT '角色表',
  KEY `idx_role_ss_user_role` (`role_id`) USING BTREE,
  CONSTRAINT `fk_role_ss_user_role` FOREIGN KEY (`role_id`) REFERENCES `bs_role` (`id`),
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='';