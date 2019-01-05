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

import com.tysoft.repository.base.UnitRepository;
import com.tysoft.entity.base.Unit;
import com.tysoft.service.base.UnitService;

/**
 * 单位表管理服务实现类
 */
@Service
@Transactional
public class UnitServiceImpl implements UnitService {
    @Autowired
    private UnitRepository unitRepository;

	/**
	 * 查询所有单位表
	 * @return List
	 */
	@Override
    public List<Unit> queryAllUnit(){
        return this.unitRepository.findAll();
    }

	/**
	 * 保存单位表
	 * @param unit
	 * @return Unit
	 */
	@Override
    public Unit saveUnit(Unit unit){
	    return this.unitRepository.saveAndFlush(unit);
	}

	/**
	 * 根据ID获取单位表
	 * @param id
	 * @return Unit
	 */
	@Override
    public Unit findUnitById(String id){
	    return this.unitRepository.findOne(id);
	}

	/**
	 * 根据ids删除单位表
	 * @param  ids
	 */
	@Override
    public void  deleteUnitByIds(String ids){
		String[] idsArr = ids.split(",");
	    for(int i = 0; i < idsArr.length; i++) {
	        this.unitRepository.delete(idsArr[i]);
	    }
	}

	/**
	 * 根据条件查询单位表
	 * @param queryMap
	 * @param sort
	 * @param sort
	 * @return List
	 */
	@Override
    public List<Unit> queryUnitList(Map<String,String> queryMap,String searchText,Sort sort){
		return this.unitRepository.findAll(genSpecification(queryMap, searchText), sort);
	}

	/**
	 * 根据条件分页查询单位表
	 * @param queryMap
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
	@Override
    public Map<String,Object> queryPagesByMap(Map<String,String> queryMap,String searchText,Pageable pageable){
		Page<Unit> page = this.unitRepository.findAll(genSpecification(queryMap, searchText), pageable);
	    return pageToMap(page);
	}

	/**
	 * 根据条件分页查询单位表
	 * @param searchText
	 * @param pageable
	 * @return Map<String,Object>
	 */
	@Override
    public Map<String,Object> queryPages(String searchText,Pageable pageable){
		searchText = StringUtils.isNotBlank(searchText)? "%"+searchText+"%":"%%";
		Page<Unit> page = this.unitRepository.queryPage(searchText, pageable);
	    return pageToMap(page);
	}


	/**
	 * 加过滤条件单位表
	 * @param queryMap
	 * @param searchText
	 * @return Specification
	 */
	 private Specification<Unit> genSpecification(Map<String, String> queryMap, String searchText) {
	     return new Specification<Unit>() {
	         @Override
	         public Predicate toPredicate(Root<Unit> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
	             List<Predicate> predicate = new ArrayList<>();
	             // TODO 加过滤条件
	             if(queryMap != null) {
	                 if(StringUtils.isNotBlank(queryMap.get("query_unitName_like"))){
	                     predicate.add(cb.like(root.get("unitName"), "%"+queryMap.get("query_unitName_like")+"%"));
	                 }
	             }

	             if(StringUtils.isNotBlank(searchText)){
	                 predicate.add(cb.like(root.get("unitName"), "%"+searchText+"%"));
	             }
	             
	             Predicate[] pre = new Predicate[predicate.size()];
	             return cq.where(predicate.toArray(pre)).getRestriction();
	         }
	     };
	 }

	/**
	 * Page转Map单位表
	 * 
	 * @param pageInfo
	 * @return Map<String,Object>
	 */
	private Map<String,Object> pageToMap(Page<Unit> page) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		if (page.getSize() > 0) {
			for (Unit unit : page) {
				listMap.add(unit.poToMap());
			}
		}
		resultMap.put("page", page.getTotalPages());
		resultMap.put("total", page.getTotalElements());
		resultMap.put("rows", listMap);
		return resultMap;
	}

}
