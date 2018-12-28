/**
* <p>Description: 使用单位管理</p>
* <p>Copyright: Copyright (c) 2018</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
* @author :lsk
* 创建日期 2018-4-28 17:59:36
* @version V1.0
*/

package com.tysoft.respository.security;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.security.Useunit;

/**
 * 使用单位
 */
@Repository
public interface UseunitRespository extends JpaRepository<Useunit,String>, JpaSpecificationExecutor<Useunit>{
	
}
