
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
import com.tysoft.repository.companyreport.CustomerCompanyRepository;
import com.tysoft.entity.companyreport.CustomerCompany;
import com.tysoft.service.companyreport.CustomerCompanyService;

/**
 * 客户公司设置表管理服务实现类
 */
@Service
@Transactional
public class CustomerCompanyServiceImpl implements CustomerCompanyService {
    @Autowired
    private CustomerCompanyRepository customerCompanyRepository;

	/**
	 * 查询所有客户公司设置表
	 * @return List
	 */
	@Override
    public List<CustomerCompany> queryAllCustomerCompany(){
        return this.customerCompanyRepository.findAll();
    }

	/**
	 * 保存客户公司设置表
	 * @param customerCompany
	 * @return CustomerCompany
	 */
	@Override
    public CustomerCompany saveCustomerCompany(CustomerCompany customerCompany){
	    return this.customerCompanyRepository.saveAndFlush(customerCompany);
	}

	/**
	 * 根据ID获取客户公司设置表
	 * @param id
	 * @return CustomerCompany
	 */
	@Override
    public CustomerCompany findCustomerCompanyById(String id){
	    return this.customerCompanyRepository.findOne(id);
	}

	/**
	 * 根据ids删除客户公司设置表
	 * @param  ids
	 */
	@Override
    public void  deleteCustomerCompanyByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.customerCompanyRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询客户公司设置表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<CustomerCompany> queryCustomerCompanyByCondition(Criteria<CustomerCompany> criteria,Sort sort){
		return this.customerCompanyRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询客户公司设置表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<CustomerCompany> queryCustomerCompanyByPage(Criteria<CustomerCompany> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.customerCompanyRepository.findAll(criteria, pageable);
	}


}
