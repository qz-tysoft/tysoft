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

import com.tysoft.repository.base.CompanyRepository;
import com.tysoft.entity.base.Company;
import com.tysoft.service.base.CompanyService;

/**
 * 公司表管理服务实现类
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

	/**
	 * 查询所有公司表
	 * @return List
	 */
	@Override
    public List<Company> queryAllCompany(){
        return this.companyRepository.findAll();
    }

	/**
	 * 保存公司表
	 * @param company
	 * @return Company
	 */
	@Override
    public Company saveCompany(Company company){
	    return this.companyRepository.saveAndFlush(company);
	}

	/**
	 * 根据ID获取公司表
	 * @param id
	 * @return Company
	 */
	@Override
    public Company findCompanyById(String id){
	    return this.companyRepository.findOne(id);
	}

	/**
	 * 根据ids删除公司表
	 * @param  ids
	 */
	@Override
    public void  deleteCompanyByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.companyRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询公司表
	 * @param queryMap
	 * @param sort
	 * @param sort
	 * @return List
	 */
	@Override
    public List<Company> queryCompanyList(Map<String,String> queryMap,String searchText,Sort sort){
		return this.companyRepository.findAll(genSpecification(queryMap, searchText), sort);
	}

	/**
	 * 根据条件分页查询公司表
	 * @param queryMap
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
	@Override
    public Map<String,Object> queryPagesByMap(Map<String,String> queryMap,String searchText,Pageable pageable){
		Page<Company> page = this.companyRepository.findAll(genSpecification(queryMap, searchText), pageable);
	    return pageToMap(page);
	}

	/**
	 * 根据条件分页查询公司表
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
	@Override
    public Map<String,Object> queryPages(String searchText,Pageable pageable){
		searchText = StringUtils.isNotBlank(searchText)? "%"+searchText+"%":"%%";
		Page<Company> page = this.companyRepository.queryPage(searchText, pageable);
	    return pageToMap(page);
	}


	/**
	 * 加过滤条件公司表
	 * @param queryMap
	 * @param searchText
	 * @return Specification
	 */
	 private Specification<Company> genSpecification(Map<String, String> queryMap, String searchText) {
	     return new Specification<Company>() {
	         @Override
	         public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	             List<Predicate> predicate = new ArrayList<>();
	             // TODO 加过滤条件
	             if(queryMap != null) {
	                 if(StringUtils.isNotBlank(queryMap.get("query_companyName_like"))){
	                     predicate.add(cb.like(root.get("companyName"), "%"+queryMap.get("query_companyName_like")+"%"));
	                 }
	             }

	             if(StringUtils.isNotBlank(searchText)){
	                 predicate.add(cb.like(root.get("companyName"), "%"+searchText+"%"));
	             }
	             
	             Predicate[] pre = new Predicate[predicate.size()];
	             return cq.where(predicate.toArray(pre)).getRestriction();
	         }
	     };
	 }

	/**
	 * Page转Map公司表
	 * 
	 * @param pageInfo
	 * @return Map<String,Object>
	 */
	private Map<String,Object> pageToMap(Page<Company> page) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		if (page.getSize() > 0) {
			for (Company company : page) {
				listMap.add(company.poToMap());
			}
		}
		resultMap.put("page", page.getTotalPages());
		resultMap.put("total", page.getTotalElements());
		resultMap.put("rows", listMap);
		return resultMap;
	}

}
