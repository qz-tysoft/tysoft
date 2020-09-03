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
import com.tysoft.repository.infomation.InfomationReceiveRepository;
import com.tysoft.entity.infomation.InfomationReceive;
import com.tysoft.service.infomation.InfomationReceiveService;

/**
 * 消息人员接收表管理服务实现类
 */
@Service
@Transactional
public class InfomationReceiveServiceImpl implements InfomationReceiveService {
    @Autowired
    private InfomationReceiveRepository infomationReceiveRepository;

	/**
	 * 查询所有消息人员接收表
	 * @return List
	 */
	@Override
    public List<InfomationReceive> queryAllInfomationReceive(){
        return this.infomationReceiveRepository.findAll();
    }

	/**
	 * 保存消息人员接收表
	 * @param infomationReceive
	 * @return InfomationReceive
	 */
	@Override
    public InfomationReceive saveInfomationReceive(InfomationReceive infomationReceive){
	    return this.infomationReceiveRepository.saveAndFlush(infomationReceive);
	}

	/**
	 * 根据ID获取消息人员接收表
	 * @param id
	 * @return InfomationReceive
	 */
	@Override
    public InfomationReceive findInfomationReceiveById(String id){
	    return this.infomationReceiveRepository.findOne(id);
	}

	/**
	 * 根据ids删除消息人员接收表
	 * @param  ids
	 */
	@Override
    public void  deleteInfomationReceiveByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.infomationReceiveRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询消息人员接收表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<InfomationReceive> queryInfomationReceiveByCondition(Criteria<InfomationReceive> criteria,Sort sort){
		return this.infomationReceiveRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询消息人员接收表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<InfomationReceive> queryInfomationReceiveByPage(Criteria<InfomationReceive> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.infomationReceiveRepository.findAll(criteria, pageable);
	}


}
