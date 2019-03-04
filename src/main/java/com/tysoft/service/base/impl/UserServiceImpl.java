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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tysoft.repository.base.UserRepository;
import com.tysoft.entity.base.User;
import com.tysoft.service.base.UserService;

/**
 * 用户表管理服务实现类
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

	/**
	 * 查询所有用户表
	 * @return List
	 */
	@Override
    public List<User> queryAllUser(){
        return this.userRepository.findAll();
    }

	/**
	 * 保存用户表
	 * @param user
	 * @return User
	 */
	@Override
    public User saveUser(User user){
	    return this.userRepository.saveAndFlush(user);
	}

	/**
	 * 根据ID获取用户表
	 * @param id
	 * @return User
	 */
	@Override
	@Cacheable(value="userCount")
    public User findUserById(String id){
	    System.out.println("redis测试");
		return this.userRepository.findOne(id);
	}

	/**
	 * 根据ids删除用户表
	 * @param  ids
	 */
	@Override
    public void  deleteUserByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.userRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询用户表
	 * @param queryMap
	 * @param sort
	 * @param sort
	 * @return List
	 */
	@Override
    public List<User> queryUserList(Map<String,String> queryMap,String searchText,Sort sort){
		return this.userRepository.findAll(genSpecification(queryMap, searchText), sort);
	}

	/**
	 * 根据条件分页查询用户表
	 * @param queryMap
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
	@Override
    public Map<String,Object> queryPagesByMap(Map<String,String> queryMap,String searchText,Pageable pageable){
		Page<User> page = this.userRepository.findAll(genSpecification(queryMap, searchText), pageable);
	    return pageToMap(page);
	}

	/**
	 * 根据条件分页查询用户表
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
	@Override
    public Map<String,Object> queryPages(String searchText,Pageable pageable){
		searchText = StringUtils.isNotBlank(searchText)? "%"+searchText+"%":"%%";
		Page<User> page = this.userRepository.queryPage(searchText, pageable);
	    return pageToMap(page);
	}


	/**
	 * 加过滤条件用户表
	 * @param queryMap
	 * @param searchText
	 * @return Specification
	 */
	 private Specification<User> genSpecification(Map<String, String> queryMap, String searchText) {
	     return new Specification<User>() {
	         @Override
	         public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	             List<Predicate> predicate = new ArrayList<>();
	             // TODO 加过滤条件
	             if(queryMap != null) {
	                 if(StringUtils.isNotBlank(queryMap.get("query_name_like"))){
	                     predicate.add(cb.like(root.get("name"), "%"+queryMap.get("query_name_like")+"%"));
	                 }
	             }

	             if(StringUtils.isNotBlank(searchText)){
	                 predicate.add(cb.like(root.get("name"), "%"+searchText+"%"));
	             }
	             
	             Predicate[] pre = new Predicate[predicate.size()];
	             return cq.where(predicate.toArray(pre)).getRestriction();
	         }
	     };
	 }

	/**
	 * Page转Map用户表
	 * 
	 * @param pageInfo
	 * @return Map<String,Object>
	 */
	private Map<String,Object> pageToMap(Page<User> page) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		if (page.getSize() > 0) {
			for (User user : page) {
				listMap.add(user.poToMap());
			}
		}
		resultMap.put("page", page.getTotalPages());
		resultMap.put("total", page.getTotalElements());
		resultMap.put("rows", listMap);
		return resultMap;
	}

}
