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
	 * @param queryMap
	 * @param searchText
	 * @param sort
	 * @return List
	 */
    public List<Unit> queryUnitList(Map<String,String> queryMap,String searchText,Sort sort);

	/**
	 * 根据条件分页查询单位表
	 * @param queryMap
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
    public Map<String,Object> queryPagesByMap(Map<String,String> queryMap,String searchText,Pageable pageable);

	/**
	 * 根据条件分页查询单位表
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
    public Map<String,Object> queryPages(String searchText,Pageable pageable);


}
