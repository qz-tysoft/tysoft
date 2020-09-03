
package com.tysoft.repository.companyreport;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.companyreport.CustomerProductDel;

/**
 * 客户产品详情表
 */
@Repository
public interface CustomerProductDelRepository extends JpaRepository<CustomerProductDel,String>, JpaSpecificationExecutor<CustomerProductDel>{
	/**
	 * 分页查询
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
	@Query("select t from CustomerProductDel t where t.productNum like ?1 or t.productPrice like ?1 or t.annexIds like ?1 or t.lesseeId like ?1 ")
    public Page<CustomerProductDel> queryPage(String searchText, Pageable pageable);


}
