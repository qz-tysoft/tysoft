/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :BearBear
* @version 1.0
*/

package com.tysoft.service.base.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tysoft.common.Criteria;
import com.tysoft.repository.base.MenuRepository;
import com.tysoft.entity.base.Menu;
import com.tysoft.service.base.MenuService;

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
}
