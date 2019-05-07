/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :BearBear
* @version 1.0
*/

package com.tysoft.service.base;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.tysoft.common.Criteria;
import com.tysoft.entity.base.Menu;

/**
 * 菜单表管理服务接口类
 */
public interface MenuService {
	/**
	 * 查询所有菜单表
	 * @return List
	 */
    public List<Menu> queryAllMenu();

	/**
	 * 保存菜单表
	 * @param menu
	 * @return Menu
	 */
    public Menu saveMenu(Menu menu);

	/**
	 * 根据ID获取菜单表
	 * @param id
	 * @return Menu
	 */
    public Menu findMenuById(String id);

	/**
	 * 根据ids删除菜单表
	 * @param  ids
	 */
    public void deleteMenuByIds(String ids);

	/**
	 * 根据条件查询菜单表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<Menu> queryMenuByCondition(Criteria<Menu> criteria,Sort sort);

	/**
	 * 根据条件分页查询菜单表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<Menu> queryMenuByPage(Criteria<Menu> criteria,Sort sort, Integer pageNo, Integer pageSize);

    public Map<String, Object> getMenuNodeMap(Menu menu);
    
    public Menu uniqueMenuByCondtion(Criteria<Menu> criteria);
    
    public Object treeMenu(List<Menu> menuList);
    
    public Object childMenu(List<Menu> menuList);
    
    public Object childMenuByFirstMenu(Menu menu);
    //菜单拼接
    public Object splitMenuMap(Map<Object,Object> map,Menu menu);
}
