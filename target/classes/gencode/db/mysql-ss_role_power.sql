-- 请另存为UTF-8编码格式后再用mysql工具导入运行该文件（建议用EditPlus打开另存为UTF-8编码）
-- Date:2019-4-30 14:55:28
-- author:BearBear

SET FOREIGN_KEY_CHECKS=0; 

DROP TABLE IF EXISTS ss_role_power;

CREATE TABLE ss_role_power (
  `role_id` VARCHAR(100) NOT NULL COMMENT '角色表',
  KEY `idx_role_ss_role_power` (`role_id`) USING BTREE,
  CONSTRAINT `fk_role_ss_role_power` FOREIGN KEY (`role_id`) REFERENCES `bs_role` (`id`),
  `power_id` VARCHAR(100) NOT NULL COMMENT '权限表',
  KEY `idx_power_ss_role_power` (`power_id`) USING BTREE,
  CONSTRAINT `fk_power_ss_role_power` FOREIGN KEY (`power_id`) REFERENCES `bs_power` (`id`),
  PRIMARY KEY (`role_id`,`power_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='';