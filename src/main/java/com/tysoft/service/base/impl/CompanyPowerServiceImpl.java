/**
* <p>Description: 1.基础数据 bs</p>
* <p>Copyright: Copyright (c) 2019</p>
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :dell
* @version 1.0
*/

package com.tysoft.service.base.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tysoft.repository.base.CompanyPowerRepository;
import com.tysoft.entity.base.CompanyPower;
import com.tysoft.service.base.CompanyPowerService;

/**
 * 公司权限表管理服务实现类
 */
@Service
@Transactional
public class CompanyPowerServiceImpl implements CompanyPowerService {
    @Autowired
    private CompanyPowerRepository companyPowerRepository;

	/**
	 * 查询所有公司权限表
	 * @return List
	 */
	@Override
    public List<CompanyPower> queryAllCompanyPower(){
        return this.companyPowerRepository.findAll();
    }

	/**
	 * 保存公司权限表
	 * @param companyPower
	 * @return CompanyPower
	 */
	@Override
    public CompanyPower saveCompanyPower(CompanyPower companyPower){
	    return this.companyPowerRepository.saveAndFlush(companyPower);
	}

	/**
	 * 根据ID获取公司权限表
	 * @param id
	 * @return CompanyPower
	 */
	@Override
    public CompanyPower findCompanyPowerById(String id){
	    return this.companyPowerRepository.findOne(id);
	}

	/**
	 * 根据ids删除公司权限表
	 * @param  ids
	 */
	@Override
    public void  deleteCompanyPowerByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.companyPowerRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询公司权限表
	 * @param queryMap
	 * @param sort
	 * @param sort
	 * @return List
	 */
	@Override
    public List<CompanyPower> queryCompanyPowerList(Map<String,String> queryMap,String searchText,Sort sort){
		return this.companyPowerRepository.findAll(genSpecification(queryMap, searchText), sort);
	}

	/**
	 * 根据条件分页查询公司权限表
	 * @param queryMap
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
	@Override
    public Map<String,Object> queryPagesByMap(Map<String,String> queryMap,String searchText,Pageable pageable){
		Page<CompanyPower> page = this.companyPowerRepository.findAll(genSpecification(queryMap, searchText), pageable);
	    return pageToMap(page);
	}

	/**
	 * 根据条件分页查询公司权限表
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
	@Override
    public Map<String,Object> queryPages(String searchText,Pageable pageable){
		searchText = StringUtils.isNotBlank(searchText)? "%"+searchText+"%":"%%";
		Page<CompanyPower> page = this.companyPowerRepository.queryPage(searchText, pageable);
	    return pageToMap(page);
	}


	/**
	 * 加过滤条件公司权限表
	 * @param queryMap
	 * @param searchText
	 * @return Specification
	 */
	 private Specification<CompanyPower> genSpecification(Map<String, String> queryMap, String searchText) {
	     return new Specification<CompanyPower>() {
	         @Override
	         public Predicate toPredicate(Root<CompanyPower> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	             List<Predicate> predicate = new ArrayList<>();
	             // TODO 加过滤条件
	             if(queryMap != null) {
	                 if(StringUtils.isNotBlank(queryMap.get("query_powerId_like"))){
	                     predicate.add(cb.like(root.get("powerId"), "%"+queryMap.get("query_powerId_like")+"%"));
	                 }
	             }

	             if(StringUtils.isNotBlank(searchText)){
	                 predicate.add(cb.like(root.get("powerId"), "%"+searchText+"%"));
	             }
	             
	             Predicate[] pre = new Predicate[predicate.size()];
	             return cq.where(predicate.toArray(pre)).getRestriction();
	         }
	     };
	 }

	/**
	 * Page转Map公司权限表
	 * 
	 * @param pageInfo
	 * @return Map<String,Object>
	 */
	private Map<String,Object> pageToMap(Page<CompanyPower> page) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		if (page.getSize() > 0) {
			for (CompanyPower companyPower : page) {
				listMap.add(companyPower.poToMap());
			}
		}
		resultMap.put("page", page.getTotalPages());
		resultMap.put("total", page.getTotalElements());
		resultMap.put("rows", listMap);
		return resultMap;
	}

}
