
package com.tysoft.service.companyreport;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.tysoft.common.Criteria;
import com.tysoft.entity.companyreport.CustomerProduct;

/**
 * 客户产品表管理服务接口类
 */
public interface CustomerProductService {
	/**
	 * 查询所有客户产品表
	 * @return List
	 */
    public List<CustomerProduct> queryAllCustomerProduct();

	/**
	 * 保存客户产品表
	 * @param customerProduct
	 * @return CustomerProduct
	 */
    public CustomerProduct saveCustomerProduct(CustomerProduct customerProduct);

	/**
	 * 根据ID获取客户产品表
	 * @param id
	 * @return CustomerProduct
	 */
    public CustomerProduct findCustomerProductById(String id);

	/**
	 * 根据ids删除客户产品表
	 * @param  ids
	 */
    public void deleteCustomerProductByIds(String ids);

	/**
	 * 根据条件查询客户产品表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<CustomerProduct> queryCustomerProductByCondition(Criteria<CustomerProduct> criteria, Sort sort);

	/**
	 * 根据条件分页查询客户产品表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<CustomerProduct> queryCustomerProductByPage(Criteria<CustomerProduct> criteria, Sort sort, Integer pageNo, Integer pageSize);


}
