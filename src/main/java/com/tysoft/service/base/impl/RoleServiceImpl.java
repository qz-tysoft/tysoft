/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :dell
* @version 1.0
*/

package com.tysoft.service.base.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tysoft.repository.base.RoleRepository;
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
	 * @param queryMap
	 * @param sort
	 * @param sort
	 * @return List
	 */
	@Override
    public List<Role> queryRoleList(Map<String,String> queryMap,String searchText,Sort sort){
		return this.roleRepository.findAll(genSpecification(queryMap, searchText), sort);
	}

	/**
	 * 根据条件分页查询角色表
	 * @param queryMap
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
	@Override
    public Map<String,Object> queryPagesByMap(Map<String,String> queryMap,String searchText,Pageable pageable){
		Page<Role> page = this.roleRepository.findAll(genSpecification(queryMap, searchText), pageable);
	    return pageToMap(page);
	}

	/**
	 * 根据条件分页查询角色表
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
	@Override
    public Map<String,Object> queryPages(String searchText,Pageable pageable){
		searchText = StringUtils.isNotBlank(searchText)? "%"+searchText+"%":"%%";
		Page<Role> page = this.roleRepository.queryPage(searchText, pageable);
	    return pageToMap(page);
	}


	/**
	 * 加过滤条件角色表
	 * @param queryMap
	 * @param searchText
	 * @return Specification
	 */
	 private Specification<Role> genSpecification(Map<String, String> queryMap, String searchText) {
	     return new Specification<Role>() {
	         @Override
	         public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	             List<Predicate> predicate = new ArrayList<>();
	             // TODO 加过滤条件
	             if(queryMap != null) {
	                 if(StringUtils.isNotBlank(queryMap.get("query_roleName_like"))){
	                     predicate.add(cb.like(root.get("roleName"), "%"+queryMap.get("query_roleName_like")+"%"));
	                 }
	             }

	             if(StringUtils.isNotBlank(searchText)){
	                 predicate.add(cb.like(root.get("roleName"), "%"+searchText+"%"));
	             }
	             
	             Predicate[] pre = new Predicate[predicate.size()];
	             return cq.where(predicate.toArray(pre)).getRestriction();
	         }
	     };
	 }

	/**
	 * Page转Map角色表
	 * 
	 * @param pageInfo
	 * @return Map<String,Object>
	 */
	private Map<String,Object> pageToMap(Page<Role> page) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		if (page.getSize() > 0) {
			for (Role role : page) {
				listMap.add(role.poToMap());
			}
		}
		resultMap.put("page", page.getTotalPages());
		resultMap.put("total", page.getTotalElements());
		resultMap.put("rows", listMap);
		return resultMap;
	}

}
