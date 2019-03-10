/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company:tysoft</p>
*
* @author :BearBear
* @version 1.0
*/

package com.tysoft.service.base.impl;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tysoft.common.Criteria;
import com.tysoft.common.Restrictions;
import com.tysoft.repository.base.UnitRepository;
import com.tysoft.entity.base.Unit;
import com.tysoft.service.base.UnitService;

/**
 * 单位表管理服务实现类
 */
@Service
@Transactional
public class UnitServiceImpl implements UnitService {
    @Autowired
    private UnitRepository unitRepository;

	/**
	 * 查询所有单位表
	 * @return List
	 */
	@Override
    public List<Unit> queryAllUnit(){
        return this.unitRepository.findAll();
    }

	/**
	 * 保存单位表
	 * @param unit
	 * @return Unit
	 */
	@Override
    public Unit saveUnit(Unit unit){
	    return this.unitRepository.saveAndFlush(unit);
	}

	/**
	 * 根据ID获取单位表
	 * @param id
	 * @return Unit
	 */
	@Override
    public Unit findUnitById(String id){
	    return this.unitRepository.findOne(id);
	}

	/**
	 * 根据ids删除单位表
	 * @param  ids
	 */
	@Override
    public void  deleteUnitByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.unitRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询单位表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<Unit> queryUnitByCondition(Criteria<Unit> criteria,Sort sort){
		return this.unitRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询单位表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<Unit> queryUnitByPage(Criteria<Unit> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.unitRepository.findAll(criteria, pageable);
	}

    public Unit findUnitByName(String unitName) {
    	 Criteria<Unit> criteria=new Criteria<>();
         criteria.add(Restrictions.eq("unitName", unitName, false));
         Unit unit=this.unitRepository.findOne(criteria);
         return unit;
    }

	
	
}
