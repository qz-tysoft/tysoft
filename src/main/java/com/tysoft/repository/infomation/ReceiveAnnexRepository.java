/**
* <p>Description: 接收的附件表管理</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
* @author :admin
* 创建日期 2019-7-22 8:57:01
* @version V1.0
*/

package com.tysoft.repository.infomation;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tysoft.entity.infomation.ReceiveAnnex;

/**
 * 接收的附件表
 */
@Repository
public interface ReceiveAnnexRepository extends JpaRepository<ReceiveAnnex,String>, JpaSpecificationExecutor<ReceiveAnnex>{
	/**
	 * 分页查询
	 * @param searchText
	 * @param pageable
	 * @return Page
	 */
	@Query("select t from ReceiveAnnex t ")
    public Page<ReceiveAnnex> queryPage(String searchText,Pageable pageable);


}
