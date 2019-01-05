/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :dell
* @version 1.0
*/

package com.tysoft.service.base;


import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.tysoft.entity.base.Role;

/**
 * 角色表管理服务接口类
 */
public interface RoleService {
	/**
	 * 查询所有角色表
	 * @return List
	 */
    public List<Role> queryAllRole();

	/**
	 * 保存角色表
	 * @param role
	 * @return Role
	 */
    public Role saveRole(Role role);

	/**
	 * 根据ID获取角色表
	 * @param id
	 * @return Role
	 */
    public Role findRoleById(String id);

	/**
	 * 根据ids删除角色表
	 * @param  ids
	 */
    public void deleteRoleByIds(String ids);

	/**
	 * 根据条件查询角色表
	 * @param queryMap
	 * @param searchText
	 * @param sort
	 * @return List
	 */
    public List<Role> queryRoleList(Map<String,String> queryMap,String searchText,Sort sort);

	/**
	 * 根据条件分页查询角色表
	 * @param queryMap
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
    public Map<String,Object> queryPagesByMap(Map<String,String> queryMap,String searchText,Pageable pageable);

	/**
	 * 根据条件分页查询角色表
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
    public Map<String,Object> queryPages(String searchText,Pageable pageable);


}
