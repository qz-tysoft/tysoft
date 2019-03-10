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
import com.tysoft.entity.base.Unit;

/**
 * 单位表管理服务接口类
 */
public interface UnitService {
	/**
	 * 查询所有单位表
	 * @return List
	 */
    public List<Unit> queryAllUnit();

	/**
	 * 保存单位表
	 * @param unit
	 * @return Unit
	 */
    public Unit saveUnit(Unit unit);

	/**
	 * 根据ID获取单位表
	 * @param id
	 * @return Unit
	 */
    public Unit findUnitById(String id);

	/**
	 * 根据ids删除单位表
	 * @param  ids
	 */
    public void deleteUnitByIds(String ids);

	/**
	 * 根据条件查询单位表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<Unit> queryUnitByCondition(Criteria<Unit> criteria,Sort sort);

	/**
	 * 根据条件分页查询单位表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<Unit> queryUnitByPage(Criteria<Unit> criteria,Sort sort, Integer pageNo, Integer pageSize);

    public Unit findUnitByName(String unitName);
}
