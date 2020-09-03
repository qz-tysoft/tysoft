
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
import com.tysoft.repository.companyreport.CustomerProductRepository;
import com.tysoft.entity.companyreport.CustomerProduct;
import com.tysoft.service.companyreport.CustomerProductService;

/**
 * 客户产品表管理服务实现类
 */
@Service
@Transactional
public class CustomerProductServiceImpl implements CustomerProductService {
    @Autowired
    private CustomerProductRepository customerProductRepository;

	/**
	 * 查询所有客户产品表
	 * @return List
	 */
	@Override
    public List<CustomerProduct> queryAllCustomerProduct(){
        return this.customerProductRepository.findAll();
    }

	/**
	 * 保存客户产品表
	 * @param customerProduct
	 * @return CustomerProduct
	 */
	@Override
    public CustomerProduct saveCustomerProduct(CustomerProduct customerProduct){
	    return this.customerProductRepository.saveAndFlush(customerProduct);
	}

	/**
	 * 根据ID获取客户产品表
	 * @param id
	 * @return CustomerProduct
	 */
	@Override
    public CustomerProduct findCustomerProductById(String id){
	    return this.customerProductRepository.findOne(id);
	}

	/**
	 * 根据ids删除客户产品表
	 * @param  ids
	 */
	@Override
    public void  deleteCustomerProductByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.customerProductRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询客户产品表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<CustomerProduct> queryCustomerProductByCondition(Criteria<CustomerProduct> criteria,Sort sort){
		return this.customerProductRepository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询客户产品表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<CustomerProduct> queryCustomerProductByPage(Criteria<CustomerProduct> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.customerProductRepository.findAll(criteria, pageable);
	}


}
