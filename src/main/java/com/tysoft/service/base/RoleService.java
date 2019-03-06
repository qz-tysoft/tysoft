/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company:tysoft</p>
*
* @author :BearBear
* @version 1.0
*/

package com.tysoft.service.base;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.tysoft.common.Criteria;
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
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<Role> queryRoleByCondition(Criteria<Role> criteria,Sort sort);

	/**
	 * 根据条件分页查询角色表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<Role> queryRoleByPage(Criteria<Role> criteria,Sort sort, Integer pageNo, Integer pageSize);


}
