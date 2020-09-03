
package com.tysoft.service.companyreport;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.tysoft.common.Criteria;
import com.tysoft.entity.companyreport.CustomerCompany;

/**
 * 客户公司设置表管理服务接口类
 */
public interface CustomerCompanyService {
	/**
	 * 查询所有客户公司设置表
	 * @return List
	 */
    public List<CustomerCompany> queryAllCustomerCompany();

	/**
	 * 保存客户公司设置表
	 * @param customerCompany
	 * @return CustomerCompany
	 */
    public CustomerCompany saveCustomerCompany(CustomerCompany customerCompany);

	/**
	 * 根据ID获取客户公司设置表
	 * @param id
	 * @return CustomerCompany
	 */
    public CustomerCompany findCustomerCompanyById(String id);

	/**
	 * 根据ids删除客户公司设置表
	 * @param  ids
	 */
    public void deleteCustomerCompanyByIds(String ids);

	/**
	 * 根据条件查询客户公司设置表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<CustomerCompany> queryCustomerCompanyByCondition(Criteria<CustomerCompany> criteria, Sort sort);

	/**
	 * 根据条件分页查询客户公司设置表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<CustomerCompany> queryCustomerCompanyByPage(Criteria<CustomerCompany> criteria, Sort sort, Integer pageNo, Integer pageSize);


}
