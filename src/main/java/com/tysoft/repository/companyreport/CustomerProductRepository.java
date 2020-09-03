
package com.tysoft.repository.companyreport;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.companyreport.CustomerProduct;

/**
 * 客户产品表
 */
@Repository
public interface CustomerProductRepository extends JpaRepository<CustomerProduct,String>, JpaSpecificationExecutor<CustomerProduct>{
	/**
	 * 分页查询
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
	@Query("select t from CustomerProduct t where t.productName like ?1 or t.specs like ?1 or t.lesseeId like ?1 ")
    public Page<CustomerProduct> queryPage(String searchText, Pageable pageable);


}
