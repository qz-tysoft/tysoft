
package com.tysoft.service.base.impl;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tysoft.common.Criteria;
import com.tysoft.common.Restrictions;
import com.tysoft.entity.base.AnnexFolder;
import com.tysoft.repository.base.AnnexFolderRespository;
import com.tysoft.service.base.AnnexFolderService;


/**
 * 附件目录管理服务实现类
 */
@Service
@Transactional
public class AnnexFolderServiceImpl implements AnnexFolderService {
    @Autowired
    private AnnexFolderRespository annexFolderRespository;

	/**
	 * 查询所有附件目录
	 * @return List
	 */
	@Override
    public List<AnnexFolder> queryAllAnnexFolder(){
        return this.annexFolderRespository.findAll();
    }

	/**
	 * 保存附件目录
	 * @param annexFolder
	 * @return AnnexFolder
	 */
	@Override
    public AnnexFolder saveAnnexFolder(AnnexFolder annexFolder){
	    return this.annexFolderRespository.saveAndFlush(annexFolder);
	}

	/**
	 * 根据ID获取附件目录
	 * @param id
	 * @return AnnexFolder
	 */
	@Override
    public AnnexFolder findAnnexFolderById(Integer id){
	    return this.annexFolderRespository.findOne(id);
	}

	/**
	 * 根据ids删除附件目录
	 * @param  ids
	 */
	@Override
    public void  deleteAnnexFolderByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.annexFolderRespository.delete(Integer.parseInt(idsArr[i]));
	    }
	}

	/**
	 * 根据条件查询附件目录
	 * @param criteria
	 * @param sort
	 * @return List
	 */
	@Override
    public List<AnnexFolder> queryAnnexFolderByCondition(Criteria<AnnexFolder> criteria,Sort sort){
		return this.annexFolderRespository.findAll(criteria, sort);
	}

	/**
	 * 根据条件分页查询附件目录
	 * @param criteria
	 * @param sort
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	@Override
    public Page<AnnexFolder> queryAnnexFolderByPage(Criteria<AnnexFolder> criteria,Sort sort, Integer pageNo, Integer pageSize){
		Pageable pageable = new PageRequest(pageNo, pageSize, sort);
		return this.annexFolderRespository.findAll(criteria, pageable);
	}

	@Override
	public List<AnnexFolder> queryAnnexFolderByType(Integer type) {
		Criteria<AnnexFolder> criteria = new Criteria<AnnexFolder>();
		criteria.add(Restrictions.eq("type", type, true));
		return this.annexFolderRespository.findAll(criteria);
	}


}
