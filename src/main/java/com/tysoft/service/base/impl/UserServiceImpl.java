/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company:tysoft</p>
*
* @author :BearBear
* @version 1.0
*/

package com.tysoft.service.base.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.apache.commons.collections4.map.HashedMap;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tysoft.common.Criteria;
import com.tysoft.common.MD5Util;
import com.tysoft.common.Restrictions;
import com.tysoft.repository.base.UserRepository;
import com.tysoft.entity.base.Menu;
import com.tysoft.entity.base.Power;
import com.tysoft.entity.base.Role;
import com.tysoft.entity.base.User;
import com.tysoft.service.base.MenuService;
import com.tysoft.service.base.PowerService;
import com.tysoft.service.base.RoleService;
import com.tysoft.service.base.UserService;

/**
 * 用户表管理服务实现类
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
	protected PowerService powerService;
	@Autowired
	protected RoleService roleService;
	@Autowired
	protected MenuService menuService;
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
    
    
    /**
     * 查看用户是否存在
     * @param user
     * @return
     */
    public User findIsUser(User user) {
    	Criteria<User> criteria = new Criteria<User>();
		criteria.add(Restrictions.eq("loginName", user.getLoginName(), false));
		return this.userRepository.findOne(criteria);
    }
    
    /**
     * 查看用户菜单
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
	public Object  findUserMenu(User user) {
        //当前角色不为空
          List<Menu> firstMenuList=new ArrayList<>();
          List<Menu> menuParentList=new ArrayList<>();
          menuParentList=(List<Menu>)this.menuService.findUserAllMenu(user,0);
          Criteria<Menu> criteria=new Criteria<>();
          criteria.add(Restrictions.eq("pid", "first", false));
          Menu menu=this.menuService.uniqueMenuByCondtion(criteria);
          //将第一级菜单进行发送
          for(int i=0;i<menuParentList.size();i++) {
        	  Menu haveMenu=menuParentList.get(i);
        	  if(haveMenu.getPid().equals(menu.getId())) {
        		  firstMenuList.add(haveMenu);
        	  }
          }
    	return firstMenuList ;
    }
    
    //递归根据父菜单查询拥有的菜单,用HashMap进行保存
    public Object findChildMenuByParent(List<Object> menuList,Menu menu) {
        Criteria<Menu> criteria=new Criteria<>();
        criteria.add(Restrictions.eq("pid",menu.getId(), false));
        Order order = new Order(Direction.DESC, "id");// 根据id排序
		Sort sort = new Sort(order);
		//得到当前所有的子节点
        List<Menu> childMenu=this.menuService.queryMenuByCondition(criteria, sort);
        //循环遍历
        Map<Object,Object> map=new HashMap<>();
        if(childMenu.size()>0) {
        	map.put(menu,childMenu);
            menuList.add(map);
        }
        for(int i=0;i<childMenu.size();i++) {
        	Menu myChildMenu=childMenu.get(i);
        	findChildMenuByParent(menuList,myChildMenu);
        }
    	return menuList;
    }
  
    

}
