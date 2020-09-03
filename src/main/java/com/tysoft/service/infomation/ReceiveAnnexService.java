/**
* <p>Description: 2.消息系统 im</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :admin
* @version 1.0
*/

package com.tysoft.service.infomation;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.tysoft.common.Criteria;
import com.tysoft.entity.infomation.ReceiveAnnex;

/**
 * 接收的附件表管理服务接口类
 */
public interface ReceiveAnnexService {
	/**
	 * 查询所有接收的附件表
	 * @return List
	 */
    public List<ReceiveAnnex> queryAllReceiveAnnex();

	/**
	 * 保存接收的附件表
	 * @param receiveAnnex
	 * @return ReceiveAnnex
	 */
    public ReceiveAnnex saveReceiveAnnex(ReceiveAnnex receiveAnnex);

	/**
	 * 根据ID获取接收的附件表
	 * @param id
	 * @return ReceiveAnnex
	 */
    public ReceiveAnnex findReceiveAnnexById(String id);

	/**
	 * 根据ids删除接收的附件表
	 * @param  ids
	 */
    public void deleteReceiveAnnexByIds(String ids);

	/**
	 * 根据条件查询接收的附件表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<ReceiveAnnex> queryReceiveAnnexByCondition(Criteria<ReceiveAnnex> criteria,Sort sort);

	/**
	 * 根据条件分页查询接收的附件表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<ReceiveAnnex> queryReceiveAnnexByPage(Criteria<ReceiveAnnex> criteria,Sort sort, Integer pageNo, Integer pageSize);


}
