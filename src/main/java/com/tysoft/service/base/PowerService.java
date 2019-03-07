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
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<Power> queryPowerByCondition(Criteria<Power> criteria,Sort sort);

	/**
	 * 根据条件分页查询权限表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<Power> queryPowerByPage(Criteria<Power> criteria,Sort sort, Integer pageNo, Integer pageSize);

    public Power  parentPower(String powerName); 
}
