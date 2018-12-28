/**
* Description: 1.资源权限 SS
* Copyright: Copyright (c) 2018
* Company: 厦门路桥信息股份有限公司
*
* @author :Administrator
* @version 1.0
*/

package com.tysoft.service.security.imp;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.tysoft.common.Criteria;
import com.tysoft.common.Restrictions;
import com.tysoft.respository.security.AnnexRespository;
import com.tysoft.service.security.AnnexService;
import com.tysoft.entity.security.Annex;

/**
 * 附件表管理服务实现类
 */
@Service
@Transactional
public class AnnexServiceImpl implements AnnexService {
    @Autowired
    private AnnexRespository annexRespository;
    
    @Value("${web.upload-path}")
    private String webUploadPath;
	/**
	 * 查询所有附件表
	 * @return List
	 */
	@Override
    public List<Annex> queryAllAnnex(){
        return this.annexRespository.findAll();
    }

	/**
	 * 保存附件表
	 * @param annex
	 * @return Annex
	 */
	@Override
    public Annex saveAnnex(Annex annex){
		annex.setIsUpdate(false);
	    return this.annexRespository.saveAndFlush(annex);
	}

	/**
	 * 根据ID获取附件表
	 * @param id
	 * @return Annex
	 */
	@Override
    public Annex findAnnexById(String id){
	    return this.annexRespository.findOne(id);
	}

	/**
	 * 根据ids删除附件表
	 * @param  ids
	 */
	@Override
    public void  deleteAnnexByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	    	Annex annex = this.findAnnexById(idsArr[i]);
	    	String oldFilePath = webUploadPath.concat(annex.getRelativePath());
	    	File oldFile = new File(oldFilePath);
	    	if (oldFile.exists()) {
	    		oldFile.delete();
	    	}
	        this.annexRespository.delete(annex);
	    }
	}

	/**
	 * 根据条件查询附件表
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<Annex> queryAnnexByCondition(Criteria<Annex> criteria,Sort sort){
		return this.annexRespository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询附件表
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<Annex> queryAnnexByPage(Criteria<Annex> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.annexRespository.findAll(criteria, pageable);
	}

	@Override
	public List<Annex> queryAnnexByIds(String annexIds) {
		Criteria<Annex> criteria = new Criteria<Annex>();
		List<String> idArr = null;
		if(!StringUtils.isEmpty(annexIds)) {
			idArr = new ArrayList<String>();
			String[] ids = annexIds.split(",");
			for(String id:ids) {
				idArr.add(id);
			}
			criteria.add(Restrictions.in("id", idArr, false));
			return this.queryAnnexByCondition(criteria, new Sort(Sort.Direction.DESC,"uploadTime"));
		}else {
			return null;
		}
	}

	/**
	 * 保存附件表(不重置isUpdate)
	 * @param annex
	 * @return Annex
	 */
	@Override
	public Annex saveAnnexForSyn(Annex annex) {
	    return this.annexRespository.saveAndFlush(annex);
	}
}
