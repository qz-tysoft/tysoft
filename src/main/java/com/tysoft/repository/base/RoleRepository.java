/**
* <p>Description: 角色表管理</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: tysoft</p>
* @author :BearBear
* 创建日期 2019-3-6 14:43:57
* @version V1.0
*/

package com.tysoft.repository.base;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.base.Role;

/**
 * 角色表
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,String>, JpaSpecificationExecutor<Role>{
	/**
	 * 分页查询
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
	@Query("select t from Role t where t.roleName like ?1 ")
    public Page<Role> queryPage(String searchText,Pageable pageable);


}
