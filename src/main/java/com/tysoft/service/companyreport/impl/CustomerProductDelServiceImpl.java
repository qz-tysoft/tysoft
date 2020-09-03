
package com.tysoft.service.companyreport.impl;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tysoft.common.Criteria;
import com.tysoft.repository.companyreport.CustomerProductDelRepository;
import com.tysoft.entity.companyreport.CustomerProductDel;
import com.tysoft.service.companyreport.CustomerProductDelService;

/**
 * 客户产品详情表管理服务实现类
 */
@Service
@Transactional
public class CustomerProductDelServiceImpl implements CustomerProductDelService {
    @Autowired
    private CustomerProductDelRepository customerProductDelRepository;

	/**
	 * 查询所有客户产品详情表
	 * @return List
	 */
	@Override
    public List<CustomerProductDel> queryAllCustomerProductDel(){
        return this.customerProductDelRepository.findAll();
    }

	/**
	 * 保存客户产品详情表
	 * @param customerProductDel
	 * @return CustomerProductDel
	 */
	@Override
    public CustomerProductDel saveCustomerProductDel(CustomerProductDel customerProductDel){
	    return this.customerProductDelRepository.saveAndFlush(customerProductDel);
	}

	/**
	 * 根据ID获取客户产品详情表
	 * @param id
	 * @return CustomerProductDel
	 */
	@Override
    public CustomerProductDel findCustomerProductDelById(String id){
	    return this.customerProductDelRepository.findOne(id);
	}

	/**
	 * 根据ids删除客户产品详情表
	 * @param  ids
	 */
	@Override
    public void  deleteCustomerProductDelByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.customerProductDelRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询客户产品详情表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<CustomerProductDel> queryCustomerProductDelByCondition(Criteria<CustomerProductDel> criteria,Sort sort){
		return this.customerProductDelRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询客户产品详情表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<CustomerProductDel> queryCustomerProductDelByPage(Criteria<CustomerProductDel> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.customerProductDelRepository.findAll(criteria, pageable);
	}


}
