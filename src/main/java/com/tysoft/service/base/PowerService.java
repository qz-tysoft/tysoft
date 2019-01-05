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
import com.tysoft.entity.base.Power;

/**
 * 权限表管理服务接口类
 */
public interface PowerService {
	/**
	 * 查询所有权限表
	 * @return List
	 */
    public List<Power> queryAllPower();

	/**
	 * 保存权限表
	 * @param power
	 * @return Power
	 */
    public Power savePower(Power power);

	/**
	 * 根据ID获取权限表
	 * @param id
	 * @return Power
	 */
    public Power findPowerById(String id);

	/**
	 * 根据ids删除权限表
	 * @param  ids
	 */
    public void deletePowerByIds(String ids);

	/**
	 * 根据条件查询权限表
	 * @param queryMap
	 * @param searchText
	 * @param sort
	 * @return List
	 */
    public List<Power> queryPowerList(Map<String,String> queryMap,String searchText,Sort sort);

	/**
	 * 根据条件分页查询权限表
	 * @param queryMap
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
    public Map<String,Object> queryPagesByMap(Map<String,String> queryMap,String searchText,Pageable pageable);

	/**
	 * 根据条件分页查询权限表
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
    public Map<String,Object> queryPages(String searchText,Pageable pageable);


}
