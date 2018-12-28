/**
* <p>Description: 单位信息管理</p>
* <p>Copyright: Copyright (c) 2018</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
* @author :Administrator
* 创建日期 2018-4-12 15:35:37
* @version V1.0
*/

package com.tysoft.respository.security;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.security.CompanyInfo;

/**
 * 单位信息
 */
@Repository
public interface CompanyInfoRespository extends JpaRepository<CompanyInfo,String>, JpaSpecificationExecutor<CompanyInfo>{
	
}
