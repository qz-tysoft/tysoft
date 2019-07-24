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
import com.tysoft.repository.infomation.ReceiveAnnexRepository;
import com.tysoft.entity.infomation.ReceiveAnnex;
import com.tysoft.service.infomation.ReceiveAnnexService;

/**
 * 接收的附件表管理服务实现类
 */
@Service
@Transactional
public class ReceiveAnnexServiceImpl implements ReceiveAnnexService {
    @Autowired
    private ReceiveAnnexRepository receiveAnnexRepository;

	/**
	 * 查询所有接收的附件表
	 * @return List
	 */
	@Override
    public List<ReceiveAnnex> queryAllReceiveAnnex(){
        return this.receiveAnnexRepository.findAll();
    }

	/**
	 * 保存接收的附件表
	 * @param receiveAnnex
	 * @return ReceiveAnnex
	 */
	@Override
    public ReceiveAnnex saveReceiveAnnex(ReceiveAnnex receiveAnnex){
	    return this.receiveAnnexRepository.saveAndFlush(receiveAnnex);
	}

	/**
	 * 根据ID获取接收的附件表
	 * @param id
	 * @return ReceiveAnnex
	 */
	@Override
    public ReceiveAnnex findReceiveAnnexById(String id){
	    return this.receiveAnnexRepository.findOne(id);
	}

	/**
	 * 根据ids删除接收的附件表
	 * @param  ids
	 */
	@Override
    public void  deleteReceiveAnnexByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.receiveAnnexRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询接收的附件表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<ReceiveAnnex> queryReceiveAnnexByCondition(Criteria<ReceiveAnnex> criteria,Sort sort){
		return this.receiveAnnexRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询接收的附件表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<ReceiveAnnex> queryReceiveAnnexByPage(Criteria<ReceiveAnnex> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.receiveAnnexRepository.findAll(criteria, pageable);
	}


}
