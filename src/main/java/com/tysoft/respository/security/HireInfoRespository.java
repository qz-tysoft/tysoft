/**
* <p>Description: 企业详细信息管理</p>
* <p>Copyright: Copyright (c) 2018</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
* @author :lsk
* 创建日期 2018-5-2 11:29:52
* @version V1.0
*/

package com.tysoft.respository.security;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.security.HireInfo;

/**
 * 企业详细信息
 */
@Repository
public interface HireInfoRespository extends JpaRepository<HireInfo,String>, JpaSpecificationExecutor<HireInfo>{
	
}
