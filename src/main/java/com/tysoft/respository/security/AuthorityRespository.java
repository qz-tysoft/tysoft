/**
* Description: 权限管理
* Copyright: Copyright (c) 2018
* Company: 厦门路桥信息股份有限公司
* @author :Administrator
* 创建日期 2018-3-9 17:23:08
* @version V1.0
*/

package com.tysoft.respository.security;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.security.Authority;

/**
 * 权限
 */
@Repository
public interface AuthorityRespository extends JpaRepository<Authority,String>, JpaSpecificationExecutor<Authority>{
	
	@Query("select max(t.sort) from Authority t where t.authorityType=:authorityType and t.parentId=:parentId")
	public Integer findMaxSort(@Param("authorityType")String authorityType,@Param("parentId")String parentId);
	
	@Query("select max(t.sort) from Authority t where t.authorityType=:authorityType")
	public Integer findMaxSort(@Param("authorityType")String authorityType);
}
