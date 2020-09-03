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
import com.tysoft.entity.infomation.Infomation;

/**
 * 消息主表管理服务接口类
 */
public interface InfomationService {
	/**
	 * 查询所有消息主表
	 * @return List
	 */
    public List<Infomation> queryAllInfomation();

	/**
	 * 保存消息主表
	 * @param infomation
	 * @return Infomation
	 */
    public Infomation saveInfomation(Infomation infomation);

	/**
	 * 根据ID获取消息主表
	 * @param id
	 * @return Infomation
	 */
    public Infomation findInfomationById(String id);

	/**
	 * 根据ids删除消息主表
	 * @param  ids
	 */
    public void deleteInfomationByIds(String ids);

	/**
	 * 根据条件查询消息主表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<Infomation> queryInfomationByCondition(Criteria<Infomation> criteria,Sort sort);

	/**
	 * 根据条件分页查询消息主表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<Infomation> queryInfomationByPage(Criteria<Infomation> criteria,Sort sort, Integer pageNo, Integer pageSize);


}
