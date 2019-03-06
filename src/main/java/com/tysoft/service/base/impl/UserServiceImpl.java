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
import com.tysoft.common.MD5Util;
import com.tysoft.common.Restrictions;
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
    public User findUserById(String id){
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
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<User> queryUserByCondition(Criteria<User> criteria,Sort sort){
		return this.userRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询用户表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<User> queryUserByPage(Criteria<User> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.userRepository.findAll(criteria, pageable);
	}
	
	/**
     * 根据用户密码查询用户
     * @param loginName
     * @param loginPwd
     * @return
     */
    public User findUser(String loginName,String loginPwd) {
    	Criteria<User> criteria = new Criteria<User>();
		criteria.add(Restrictions.eq("loginName", loginName, false));
		criteria.add(Restrictions.eq("loginPsw", MD5Util.encode(loginPwd), false));
		criteria.add(Restrictions.eq("state", 0, false));
		return this.userRepository.findOne(criteria);
    }


}
