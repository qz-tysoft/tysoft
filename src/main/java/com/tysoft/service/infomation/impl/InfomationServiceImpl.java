/**
* <p>Description: 2.消息系统 im</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :admin
* @version 1.0
*/

package com.tysoft.service.infomation.impl;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tysoft.common.Criteria;
import com.tysoft.repository.infomation.InfomationRepository;
import com.tysoft.entity.infomation.Infomation;
import com.tysoft.service.infomation.InfomationService;

/**
 * 消息主表管理服务实现类
 */
@Service
@Transactional
public class InfomationServiceImpl implements InfomationService {
    @Autowired
    private InfomationRepository infomationRepository;

	/**
	 * 查询所有消息主表
	 * @return List
	 */
	@Override
    public List<Infomation> queryAllInfomation(){
        return this.infomationRepository.findAll();
    }

	/**
	 * 保存消息主表
	 * @param infomation
	 * @return Infomation
	 */
	@Override
    public Infomation saveInfomation(Infomation infomation){
	    return this.infomationRepository.saveAndFlush(infomation);
	}

	/**
	 * 根据ID获取消息主表
	 * @param id
	 * @return Infomation
	 */
	@Override
    public Infomation findInfomationById(String id){
	    return this.infomationRepository.findOne(id);
	}

	/**
	 * 根据ids删除消息主表
	 * @param  ids
	 */
	@Override
    public void  deleteInfomationByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.infomationRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询消息主表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<Infomation> queryInfomationByCondition(Criteria<Infomation> criteria,Sort sort){
		return this.infomationRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询消息主表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<Infomation> queryInfomationByPage(Criteria<Infomation> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.infomationRepository.findAll(criteria, pageable);
	}


}
