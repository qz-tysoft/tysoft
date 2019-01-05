/**
* <p>Description: 公司权限表管理</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
* @author :dell
* 创建日期 2019-1-4 22:45:47
* @version V1.0
*/

package com.tysoft.repository.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.base.CompanyPower;

/**
 * 公司权限表
 */
@Repository
public interface CompanyPowerRepository extends JpaRepository<CompanyPower,String>, JpaSpecificationExecutor<CompanyPower>{
	/**
	 * 分页查询
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
	@Query("select t from CompanyPower t where t.powerId like ?1 or t.companyId like ?1 ")
    public Page<CompanyPower> queryPage(String searchText,Pageable pageable);


}
