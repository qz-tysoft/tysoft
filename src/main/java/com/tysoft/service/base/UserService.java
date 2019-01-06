/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :dell
* @version 1.0
*/

package com.tysoft.service.base;


import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.tysoft.entity.base.User;

/**
 * 用户表管理服务接口类
 */
public interface UserService {
	/**
	 * 查询所有用户表
	 * @return List
	 */
    public List<User> queryAllUser();

	/**
	 * 保存用户表
	 * @param user
	 * @return User
	 */
    public User saveUser(User user);

	/**
	 * 根据ID获取用户表
	 * @param id
	 * @return User
	 */
    public User findUserById(String id);

	/**
	 * 根据ids删除用户表
	 * @param  ids
	 */
    public void deleteUserByIds(String ids);

	/**
	 * 根据条件查询用户表
	 * @param queryMap
	 * @param searchText
	 * @param sort
	 * @return List
	 */
    public List<User> queryUserList(Map<String,String> queryMap,String searchText,Sort sort);

	/**
	 * 根据条件分页查询用户表
	 * @param queryMap
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
    public Map<String,Object> queryPagesByMap(Map<String,String> queryMap,String searchText,Pageable pageable);

	/**
	 * 根据条件分页查询用户表
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
    public Map<String,Object> queryPages(String searchText,Pageable pageable);


}