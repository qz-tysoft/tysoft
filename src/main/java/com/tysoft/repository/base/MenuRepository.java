/**
* <p>Description: 菜单表管理</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
* @author :BearBear
* 创建日期 2019-4-25 9:16:22
* @version V1.0
*/

package com.tysoft.repository.base;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.base.Menu;

/**
 * 菜单表
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu,String>, JpaSpecificationExecutor<Menu>{
	/**
	 * 分页查询
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
	@Query("select t from Menu t where t.menuName like ?1 or t.pid like ?1 ")
    public Page<Menu> queryPage(String searchText,Pageable pageable);


}
