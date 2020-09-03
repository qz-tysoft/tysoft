
package com.tysoft.repository.companyreport;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.companyreport.CustomerCompany;

/**
 * 客户公司设置表
 */
@Repository
public interface CustomerCompanyRepository extends JpaRepository<CustomerCompany,String>, JpaSpecificationExecutor<CustomerCompany>{
	/**
	 * 分页查询
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
	@Query("select t from CustomerCompany t where t.companyName like ?1 or t.lesseeId like ?1 ")
    public Page<CustomerCompany> queryPage(String searchText, Pageable pageable);


}
