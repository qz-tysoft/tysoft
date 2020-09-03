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
import com.tysoft.entity.infomation.InfomationReceive;

/**
 * 消息人员接收表管理服务接口类
 */
public interface InfomationReceiveService {
	/**
	 * 查询所有消息人员接收表
	 * @return List
	 */
    public List<InfomationReceive> queryAllInfomationReceive();

	/**
	 * 保存消息人员接收表
	 * @param infomationReceive
	 * @return InfomationReceive
	 */
    public InfomationReceive saveInfomationReceive(InfomationReceive infomationReceive);

	/**
	 * 根据ID获取消息人员接收表
	 * @param id
	 * @return InfomationReceive
	 */
    public InfomationReceive findInfomationReceiveById(String id);

	/**
	 * 根据ids删除消息人员接收表
	 * @param  ids
	 */
    public void deleteInfomationReceiveByIds(String ids);

	/**
	 * 根据条件查询消息人员接收表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<InfomationReceive> queryInfomationReceiveByCondition(Criteria<InfomationReceive> criteria,Sort sort);

	/**
	 * 根据条件分页查询消息人员接收表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<InfomationReceive> queryInfomationReceiveByPage(Criteria<InfomationReceive> criteria,Sort sort, Integer pageNo, Integer pageSize);


}
