/**
* Description: 管理人员类型管理
* Copyright: Copyright (c) 2018
* Company: 厦门路桥信息股份有限公司
* @author :Administrator
* 创建日期 2018-3-29 11:34:29
* @version V1.0
*/

package com.tysoft.respository.security;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.security.AdminType;

/**
 * 管理人员类型
 */
@Repository
public interface AdminTypeRespository extends JpaRepository<AdminType,String>, JpaSpecificationExecutor<AdminType>{
	
}
