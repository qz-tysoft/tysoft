/**
* Description: 附件表管理
* Copyright: Copyright (c) 2018
* Company: 厦门路桥信息股份有限公司
* @author :Administrator
* 创建日期 2018-3-9 17:23:08
* @version V1.0
*/

package com.tysoft.respository.security;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.security.Annex;

/**
 * 附件表
 */
@Repository
public interface AnnexRespository extends JpaRepository<Annex,String>, JpaSpecificationExecutor<Annex>{
	
}
