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
import com.tysoft.repository.base.PowerRepository;
import com.tysoft.entity.base.Power;
import com.tysoft.service.base.PowerService;

/**
 * 权限表管理服务实现类
 */
@Service
@Transactional
public class PowerServiceImpl implements PowerService {
    @Autowired
    private PowerRepository powerRepository;

	/**
	 * 查询所有权限表
	 * @return List
	 */
	@Override
    public List<Power> queryAllPower(){
        return this.powerRepository.findAll();
    }

	/**
	 * 保存权限表
	 * @param power
	 * @return Power
	 */
	@Override
    public Power savePower(Power power){
	    return this.powerRepository.saveAndFlush(power);
	}

	/**
	 * 根据ID获取权限表
	 * @param id
	 * @return Power
	 */
	@Override
    public Power findPowerById(String id){
	    return this.powerRepository.findOne(id);
	}

	/**
	 * 根据ids删除权限表
	 * @param  ids
	 */
	@Override
    public void  deletePowerByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.powerRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询权限表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<Power> queryPowerByCondition(Criteria<Power> criteria,Sort sort){
		return this.powerRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询权限表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<Power> queryPowerByPage(Criteria<Power> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.powerRepository.findAll(criteria, pageable);
	}
	 
	 @Override
	 public Power  parentPower(String powerName) {
		  Criteria<Power> criteria=new Criteria<>();
		  criteria.add(Restrictions.eq("powerName", powerName, false));
		  criteria.add(Restrictions.eq("pid", "menu", false));
		  return this.powerRepository.findOne(criteria);
	  }

}
