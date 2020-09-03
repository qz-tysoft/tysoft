/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company:tysoft</p>
*
* @author :BearBear
* @version 1.0
*/

package com.tysoft.service.base.impl;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tysoft.common.Criteria;
import com.tysoft.common.Restrictions;
import com.tysoft.repository.base.RoleRepository;
import com.tysoft.entity.base.Power;
import com.tysoft.entity.base.Role;
import com.tysoft.service.base.RoleService;

/**
 * 角色表管理服务实现类
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

	/**
	 * 查询所有角色表
	 * @return List
	 */
	@Override
    public List<Role> queryAllRole(){
        return this.roleRepository.findAll();
    }

	/**
	 * 保存角色表
	 * @param role
	 * @return Role
	 */
	@Override
    public Role saveRole(Role role){
	    return this.roleRepository.saveAndFlush(role);
	}

	/**
	 * 根据ID获取角色表
	 * @param id
	 * @return Role
	 */
	@Override
    public Role findRoleById(String id){
	    return this.roleRepository.findOne(id);
	}

	/**
	 * 根据ids删除角色表
	 * @param  ids
	 */
	@Override
    public void  deleteRoleByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.roleRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询角色表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<Role> queryRoleByCondition(Criteria<Role> criteria,Sort sort){
		return this.roleRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询角色表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<Role> queryRoleByPage(Criteria<Role> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.roleRepository.findAll(criteria, pageable);
	}

	 public Role isExistRole(String roleName) {
		  Criteria<Role> criteria=new Criteria<>();
		  criteria.add(Restrictions.eq("roleName", roleName, false));
		  return this.roleRepository.findOne(criteria);
	 }
}
