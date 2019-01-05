/**
* <p>Description: 权限表管理</p>
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

import com.tysoft.entity.base.Power;

/**
 * 权限表
 */
@Repository
public interface PowerRepository extends JpaRepository<Power,String>, JpaSpecificationExecutor<Power>{
	/**
	 * 分页查询
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
	@Query("select t from Power t where t.powerName like ?1 or t.url like ?1 or t.companyId like ?1 ")
    public Page<Power> queryPage(String searchText,Pageable pageable);


}
