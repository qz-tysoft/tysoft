/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company:tysoft</p>
*
* @author :BearBear
* @version 1.0
*/

package com.tysoft.service.base;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.tysoft.common.Criteria;
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
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<User> queryUserByCondition(Criteria<User> criteria,Sort sort);

	/**
	 * 根据条件分页查询用户表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<User> queryUserByPage(Criteria<User> criteria,Sort sort, Integer pageNo, Integer pageSize);

    /**
     * 根据用户密码查询用户
     * @param loginName
     * @param loginPwd
     * @return
     */
    public User findUser(String loginName,String loginPwd);
    
    /**
     * 判断用户是否存在
     * @param user
     * @return
     */
    public User findIsUser(User user);
    
    
    public Object findUserMenu(User user);
    
}
