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
import com.tysoft.entity.base.AnnexFolder;

/**
 * 附件目录管理服务接口类
 */
public interface AnnexFolderService {
	/**
	 * 查询所有附件目录
	 * @return List
	 */
    public List<AnnexFolder> queryAllAnnexFolder();

	/**
	 * 保存附件目录
	 * @param annexFolder
	 * @return AnnexFolder
	 */
    public AnnexFolder saveAnnexFolder(AnnexFolder annexFolder);

	/**
	 * 根据ID获取附件目录
	 * @param id
	 * @return AnnexFolder
	 */
    public AnnexFolder findAnnexFolderById(Integer id);

	/**
	 * 根据ids删除附件目录
	 * @param  ids
	 */
    public void deleteAnnexFolderByIds(String ids);

	/**
	 * 根据条件查询附件目录
	 * @param criteria
	 * @param sort
	 * @return List
	 */
    public List<AnnexFolder> queryAnnexFolderByCondition(Criteria<AnnexFolder> criteria,Sort sort);

	/**
	 * 根据条件分页查询附件目录
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
    public Page<AnnexFolder> queryAnnexFolderByPage(Criteria<AnnexFolder> criteria,Sort sort, Integer pageNo, Integer pageSize);

    /**
     * 根据类型获取附件目录
     * @param type
     * @return
     */
    public List<AnnexFolder> queryAnnexFolderByType(Integer type);
}
