
package com.tysoft.service.companyreport;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.tysoft.common.Criteria;
import com.tysoft.entity.companyreport.CustomerProductDel;

/**
 * 客户产品详情表管理服务接口类
 */
public interface CustomerProductDelService {
	/**
	 * 查询所有客户产品详情表
	 * @return List
	 */
    public List<CustomerProductDel> queryAllCustomerProductDel();

	/**
	 * 保存客户产品详情表
	 * @param customerProductDel
	 * @return CustomerProductDel
	 */
    public CustomerProductDel saveCustomerProductDel(CustomerProductDel customerProductDel);

	/**
	 * 根据ID获取客户产品详情表
	 * @param id
	 * @return CustomerProductDel
	 */
    public CustomerProductDel findCustomerProductDelById(String id);

	/**
	 * 根据ids删除客户产品详情表
	 * @param  ids
	 */
    public void deleteCustomerProductDelByIds(String ids);

	/**
	 * 根据条件查询客户产品详情表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<CustomerProductDel> queryCustomerProductDelByCondition(Criteria<CustomerProductDel> criteria, Sort sort);

	/**
	 * 根据条件分页查询客户产品详情表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<CustomerProductDel> queryCustomerProductDelByPage(Criteria<CustomerProductDel> criteria, Sort sort, Integer pageNo, Integer pageSize);


}
