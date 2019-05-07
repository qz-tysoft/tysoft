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
        List<Role> roles=user.getRoles();
        //存储所有的权限
        List<Power> powerList=new ArrayList<>();
        //当前角色不为空
        List<Menu> menuParentList=new ArrayList<>();
        if(roles.size()>0) {
          //拿出每个角色对应的权限
          for(int i=0;i<roles.size();i++) {
             Role role=roles.get(i);
             List<Power> powers=role.getPowers();
             if(powers.size()>0) {
            	 for(int j=0;j<powers.size();j++) {
            		 powerList.add(powers.get(j));
            	 }
             }
          }
          
          //根据权限查询所有子菜单
          List<Menu> menuList=new ArrayList<>();
          for(int i=0;i<powerList.size();i++) {
        	  Power power=powerList.get(i);
        	  if(power!=null) {
        	  Criteria<Menu> criteria=new Criteria<>();
        	  criteria.add(Restrictions.eq("power", power, false));
        	  List<Menu> menus=this.menuService.queryMenuByCondition(criteria, null);
        	  for(int j=0;j<menus.size();j++) {
        		  Menu menu=menus.get(j);
        		  menuList.add(menu);
        	  }
        	
        	  }
          }
          //根据子菜单获取菜单树
          List<Object> userMenuList=new ArrayList<>();
          //查到所有拥有子菜单的级数
          menuParentList=(List<Menu>)this.menuService.treeMenu(menuList);
          Criteria<Menu> criteria=new Criteria<>();
          criteria.add(Restrictions.eq("pid", "first", false));
    	  Menu menu=this.menuService.uniqueMenuByCondtion(criteria);
          String pid=menu.getId();
          for(int i=0;i<menuParentList.size();i++) {
        	  Menu parentMenu=menuParentList.get(i);
              if(parentMenu.getPid().equals(pid)) {
            	  //拿到一级菜单
            	  List<Object> childMenuByParent=(List<Object>) findChildMenuByParent(new ArrayList<>(),parentMenu);
            	  //根据一级菜单查询所有子菜单
            	  int mySize=childMenuByParent.size();
            	  //拿出来一个个比较不存在进行删除
            	  List<Object> newChildMenuMap=new ArrayList<>();
            	  //有二级先添加没有二级后添加
            	  //当前只有一级
            	  if(mySize==1) {
            	     //拿出第一级的数据
            		 HashMap<Object,Object> hashMap=(HashMap<Object, Object>) childMenuByParent.get(0);
            		 List<Menu> allMenuList=  (List<Menu>) hashMap.get(parentMenu);
            		 for(int i1=0;i1<allMenuList.size();i1++) {
            			 Menu childMenu=allMenuList.get(i1);
            			 if(menuList.contains(childMenu)) {
            				 HashMap<Object, Object> lastMap=new HashMap<>();
             				 List<Menu> newChildMenu=new ArrayList<>();
             				 newChildMenu.add(childMenu);
             				 lastMap.put(childMenu, childMenu);
             				 newChildMenuMap.add(lastMap);
            			 }
            		 }
            	  }
            	  //里面还有多级
            	  if(mySize>1) {
            	  //进行比较
                  //将第一个删除得到当前所有的菜单
            		childMenuByParent.remove(0);
            		 for(int j=0;j<childMenuByParent.size();j++) {
            		  //拥有二级菜单的map
            		  Map<Object,Object> map=(HashMap<Object, Object>)childMenuByParent.get(j);
            		  //拥有二级菜单拥有子菜单
            		  for(int k=1;k<menuParentList.size();k++) {
            			  //包含子菜单
            			  List<Menu> newChildMenu=new ArrayList<>();
            			  if(map.containsKey(menuParentList.get(k))) {
            				 //拿出所有的权限 
            				 List<Menu> menus=(List<Menu>) map.get(menuParentList.get(k));
            				 //所拥有的子菜单
            				 for(int k1=0;k1<menuList.size();k1++) {
            					 Menu childMenu=menuList.get(k1);
            					 if(menus.contains(childMenu)) {
            						 if(!newChildMenu.contains(childMenu)) {
            							 newChildMenu.add(childMenu);
            						 }
            						 menuList.remove(childMenu);
            					 }
            				 }
            				 //进行重新添加得到拥有的子菜单
            				 HashMap<Object, Object> newMap=new HashMap<>();
            				 newMap.put(menuParentList.get(k), newChildMenu);
            				 newChildMenuMap.add(newMap);
            			  }
            		  }
            		  //没有二级菜单的后添加-排除掉二级的
            		  for(int k2=0;k2<menuList.size();k2++) {
         				 Menu last=menuList.get(k2);
         				 if(last.getPid().equals(parentMenu.getId())) {
         					 HashMap<Object, Object> lastMap=new HashMap<>();
             				 List<Menu> newChildMenu=new ArrayList<>();
             				 newChildMenu.add(last);
             				 lastMap.put(last.getMenuName(), last.getMenuName());
             				 if(newChildMenuMap.contains(lastMap)==false) {
             					 newChildMenuMap.add(lastMap);	 
             				 }
         				 }
            		  }
            	  }
              }
            	  System.out.println("newChildMenuMap:"+newChildMenuMap);
        	 }
          }
        }
    	return menuParentList ;
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
