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
import com.tysoft.entity.base.User;

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
    
    public Object treeMenu(List<Menu> menuList,int type);
    
    public Object childMenu(List<Menu> menuList);
    
    public List<Menu> childMenuByFirstMenu(String pid);
    //查询当前用户所拥有的所有菜单
    public List<Menu> findUserAllMenu(User user,int type);
    //根据父id查询拥有的子菜单那
    public Map<String,Object> findChildMenuByPid(String pid,User user);
    //根据所有子菜单得到完整的菜单
    public List<Menu> allMenuByChildMenu(List<Menu> childMenus);
    //递归根据父菜单递归得到所有子菜单
    public List<Menu> getAllMenuByFatherMeun(List<Menu> menus);
    //递归根据子菜单递归得到顶层父菜单
    public Menu getFatherMenuByChildMenu(Menu menu);

    
}
