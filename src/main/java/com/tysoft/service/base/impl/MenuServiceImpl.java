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
	  
	    public Object treeMenu(List<Menu> menuList) {
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
	    	for(int i=0;i<menuList.size();i++) {
	    		Menu  childMenu=menuList.get(i);
	    		if(allMenu.indexOf(childMenu)!=-1) {
	    			allMenu.remove(childMenu);
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
	    
	    public Object childMenuByFirstMenu(Menu menu) {
	    	String id=menu.getId();
	    	Criteria<Menu> criteria=new Criteria<>();
	    	criteria.add(Restrictions.eq("pid", id, false));
	    	Order order = new Order(Direction.DESC, "id");// 根据id排序
	 		Sort sort = new Sort(order);
	    	List<Menu> menus=this.queryMenuByCondition(criteria, sort);
	    	return menus;
	    }
	
	    //菜单拼接
	    @SuppressWarnings("unchecked")
	    public Object splitMenuMap(Map<Object,Object> map,Menu menu) {
	    	Iterator<Entry<Object, Object>> it = map.entrySet().iterator();
			String allPara = "";
			List<Object> splitObj=new ArrayList<>();
			List<Object> obj=new ArrayList<>();
			List<Menu> keyList=new ArrayList<>();
			List<Menu> menus=(List<Menu>) this.childMenuByFirstMenu(menu);
			while (it.hasNext()) {
				Entry<Object, Object> entry = it.next();
				Menu child = (Menu) entry.getKey();
				List<Menu> menuList=new ArrayList<>();
				menuList.add(child);
				//查询子菜单
				List<Menu> menuChild=(List<Menu>) this.childMenu(menuList);
				obj.add(menuChild);
				keyList.add(child);
			}
			
			for(int i=0;i<obj.size();i++) {
				List<Menu> menuList=(List<Menu>) obj.get(i);
				//单独一个菜单
				if(menuList.size()==1) {
					if(keyList.contains(menuList.get(0))) {
					if(StringUtil.isNotBlank(menuList.get(0).getPower().getUrl())) {
					List<Object> childObj=new ArrayList<>();
					HashMap<Object,List<Object>> hashMap=new HashMap<>();
					HashMap <Object,Object> childMap=new HashMap<>();
					childMap.put(menuList.get(0).getMenuName(), menuList.get(0).getPower().getUrl());
					childObj.add(childMap);
					hashMap.put(menuList.get(0).getMenuName(), childObj);
					splitObj.add(hashMap);
				  }
				}
			  }
			    //多个菜单情况
				if(menuList.size()>1) {
				   Menu haveMenu=keyList.get(i);
				    
				}
			}
			
	    	return null;
	    }
}
