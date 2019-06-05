/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :BearBear
* @version 1.0
*/

package com.tysoft.service.base.impl;


import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tysoft.common.Criteria;
import com.tysoft.common.Restrictions;
import com.tysoft.repository.base.MenuRepository;
import com.tysoft.entity.base.Menu;
import com.tysoft.entity.base.Power;
import com.tysoft.entity.base.Role;
import com.tysoft.entity.base.User;
import com.tysoft.service.base.MenuService;

import jodd.util.StringUtil;

/**
 * 菜单表管理服务实现类
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

	/**
	 * 查询所有菜单表
	 * @return List
	 */
	@Override
    public List<Menu> queryAllMenu(){
        return this.menuRepository.findAll();
    }

	/**
	 * 保存菜单表
	 * @param menu
	 * @return Menu
	 */
	@Override
    public Menu saveMenu(Menu menu){
	    return this.menuRepository.saveAndFlush(menu);
	}

	/**
	 * 根据ID获取菜单表
	 * @param id
	 * @return Menu
	 */
	@Override
    public Menu findMenuById(String id){
	    return this.menuRepository.findOne(id);
	}

	/**
	 * 根据ids删除菜单表
	 * @param  ids
	 */
	@Override
    public void  deleteMenuByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.menuRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询菜单表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<Menu> queryMenuByCondition(Criteria<Menu> criteria,Sort sort){
		return this.menuRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询菜单表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<Menu> queryMenuByPage(Criteria<Menu> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.menuRepository.findAll(criteria, pageable);
	}

	public Map<String, Object> getMenuNodeMap(Menu menu){
        Map<String, Object> menuNodeMap = new HashMap<>();
        return menuNodeMap;
	}
	
	 public Menu uniqueMenuByCondtion(Criteria<Menu> criteria) {
		 
		 return this.menuRepository.findOne(criteria);
	 }
	  
	    public Object treeMenu(List<Menu> menuList,int type) {
	    	List<Menu> allMenu=new ArrayList<>();
	    	for(int i=0;i<menuList.size();i++) {
	    		Menu childMenu=menuList.get(i);
	    		List<Menu> childMenuList=new ArrayList<>();
	    		childMenuList.add(childMenu);
	    		//调用递归查询该子节点所有的菜单
	    		List<Menu> allChild=(List<Menu>) childMenu(childMenuList);
	    		for(int j=0;j<allChild.size();j++) {
	    		  //不存在
	    		  if(allMenu.indexOf(allChild.get(j))==-1){
	    			  allMenu.add(allChild.get(j));
	    		  }
	    		}
	    	}
	    	
	    	//将所有的子权限去除-得到父级
	    	if(type==1) {
	    	for(int i=0;i<menuList.size();i++) {
	    		Menu  childMenu=menuList.get(i);
	    		if(allMenu.indexOf(childMenu)!=-1) {
	    			allMenu.remove(childMenu);
	    		}
	    	}
	    	}
	    	//得到当前所有的父级菜单
	    	return allMenu;
	    }
	    
	    //递归查询这个子菜单
	    public Object childMenu(List<Menu> menuList) {
	    	int menuSize=menuList.size();
	    	//得到最后一个比较对象 
	    	Menu menu=menuList.get(menuSize-1);
	    	String pid=menu.getPid();
	        Criteria<Menu> criteria=new Criteria<>();
	        criteria.add(Restrictions.eq("id", pid, false));
	        criteria.add(Restrictions.ne("menuName", "公司菜单", false));
	        Menu pMenu =this.uniqueMenuByCondtion(criteria);
	        List<Menu> allMenu=menuList;
	        //父级菜单不为空
	        if(pMenu!=null) {
	        	allMenu.add(pMenu);
	        	childMenu(allMenu);
	        }
	        //倒序排列
	        Collections.reverse(allMenu);
	        return allMenu;
	    }
	    
	    public List<Menu> childMenuByFirstMenu(String pid) {
	    	//String id=menu.getId();
	    	Criteria<Menu> criteria=new Criteria<>();
	    	criteria.add(Restrictions.eq("pid", pid, false));
	    	Order order = new Order(Direction.DESC, "id");// 根据id排序
	 		Sort sort = new Sort(order);
	    	List<Menu> menus=this.queryMenuByCondition(criteria, sort);
	    	return menus;
	    }
	    
	    
	    public List<Menu> findUserAllMenu(User user,int type){
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
	         	  List<Menu> menus=this.queryMenuByCondition(criteria, null);
	         	  for(int j=0;j<menus.size();j++) {
	         		  Menu menu=menus.get(j);
	         		  if(menuList.contains(menu)==false) {
	         			  menuList.add(menu);
	         		  }
	         	  }
	         	 }
	           }
	           //查到所有拥有子菜单的级数
	           if(type==0) {
	           menuParentList=(List<Menu>)this.treeMenu(menuList,1);
	           }else if(type==1){
	        	   menuParentList=menuList;
	           }
	       }
			return menuParentList;
	    }
	    
	    public Map<String,Object> findChildMenuByPid(String pid,User user){
	    	//当前父菜单所拥有所有子菜单
	    	Map<String,Object> map=new HashMap<>();
	    	List<Menu> menuList =this.childMenuByFirstMenu(pid);
	    	//拥有所有的
	    	List<Menu> haveMenuList =this.findUserAllMenu(user,1);
	    	List<Menu> supplyHaveMenu =(List<Menu>)this.treeMenu(haveMenuList,1);
	    	for(int i=0;i<supplyHaveMenu.size();i++) {
	    		haveMenuList.add(supplyHaveMenu.get(i));
	    	}
	    	
	    	List<Menu> sendMenu =new ArrayList<>();
	    	List<String> sendUrl=new ArrayList<>();
	    	List<Integer>  flagList=new ArrayList<>();
	    	for(int i=0;i<haveMenuList.size();i++) {
	    		Menu menu=haveMenuList.get(i);
	    		for(int j=0;j<menuList.size();j++) {
	    			Menu isHaveMenu=menuList.get(j);
	    			if(menu==isHaveMenu) {
	    				sendMenu.add(isHaveMenu.poToVo());
	    				if(isHaveMenu.getPower()!=null) {
		    				sendUrl.add(isHaveMenu.getPower().getUrl());
	    				    System.out.println(isHaveMenu.getPower().getUrl());
	    				}else {
		    				sendUrl.add("");
	    				}
	    				//标记判断是否有子菜单
	    				List<Menu> nextChild =this.childMenuByFirstMenu(isHaveMenu.getId());
	    				if(nextChild.size()>0) {
	    					//有下级菜单
	    					flagList.add(1);
	    				}else {
	    					//无下级菜单
	    					flagList.add(0);
	    				}
	    			}
	    		}
	    	}
	     	map.put("sendUrl", sendUrl);
	    	map.put("flagList", flagList);
	    	map.put("sendMenu", sendMenu);
	   
	    	return map;
	    }
	        
	    //当前为递归
	    public List<Menu> allMenuByChildMenu(List<Menu> childMenus){
	        int  before=childMenus.size();
	    	for(int i=0;i<childMenus.size();i++) {
	    		Menu childMenu=childMenus.get(i);
	    		Criteria<Menu> criteria=new Criteria<>();
		    	criteria.add(Restrictions.eq("id", childMenu.getPid(), false));
		    	Menu pmenu=this.uniqueMenuByCondtion(criteria);
		    	if(!childMenus.contains(pmenu)) {
		    		if(!childMenu.getMenuName().equals("公司菜单")) {
			    		childMenus.add(pmenu);
		    		}
		    	}
	    	}
	    	//当前还有
	        int now=childMenus.size();
	        if(now!=before) {
	        	allMenuByChildMenu(childMenus);
	        }else {
	        	Criteria<Menu> criteria=new Criteria<>();
		    	criteria.add(Restrictions.eq("pid","first", false));
		    	Menu pmenu=this.uniqueMenuByCondtion(criteria);
		    	childMenus.remove(pmenu);
	        }
	    	return  childMenus;
	    }
}
