/**
* Description: 1.资源权限 SS
* Copyright: Copyright (c) 2018
* Company: 厦门路桥信息股份有限公司
*
* @author :Administrator
* @version 1.0
*/

package com.tysoft.service.base;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.tysoft.common.Criteria;
import com.tysoft.entity.base.Annex;

/**
 * 附件表管理服务接口类
 */
public interface AnnexService {
	/**
	 * 查询所有附件表
	 * @return List
	 */
    public List<Annex> queryAllAnnex();

	/**
	 * 保存附件表
	 * @param annex
	 * @return Annex
	 */
    public Annex saveAnnex(Annex annex);

	/**
	 * 根据ID获取附件表
	 * @param id
	 * @return Annex
	 */
    public Annex findAnnexById(String id);

	/**
	 * 根据ids删除附件表
	 * @param  ids
	 */
    public void deleteAnnexByIds(String ids);

	/**
	 * 根据条件查询附件表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<Annex> queryAnnexByCondition(Criteria<Annex> criteria,Sort sort);

	/**
	 * 根据条件分页查询附件表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<Annex> queryAnnexByPage(Criteria<Annex> criteria,Sort sort, Integer pageNo, Integer pageSize);

    /**
     * 根据ID获取附件列表
     * @param annexIds
     * @return
     */
    public List<Annex> queryAnnexByIds(String annexIds);
    
    /**
	 * 保存附件表(不重置isUpdate)
	 * @param annex
	 * @return Annex
	 */
    public Annex saveAnnexForSyn(Annex annex);
}
