/**
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 厦门市路桥信息工程有限公司</p>
 *
 * @author not attributable
 * @version 1.0
 */
package com.tysoft.entity.security;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 使用单位 用户的所属单位
 * @author 郭志峰
 * 创建日期 May 5, 2011 时间 9:35:51 AM
 */

@Entity
@Table(name = "SS_useunit")
@Inheritance(strategy=InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Useunit implements Serializable{
	private static final long serialVersionUID = -4341262952898041520L;
	public static String MANAGET_TYPE = "1";//不独立管理（不设置管理员）
	public static String NO_MANAGET_TYPE = "0";//独立管理（设置管理员）
	/**
	 * ID
	 */
	private String id;
	/**
	 * 单位名称
	 */
    private String name;
   
    /**
     * 属性 : 简称
     */
    private String shortName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 管理类型
     */
    private java.lang.String manageType = NO_MANAGET_TYPE;
    /**
     * 父单位
     */
    private String parentId;
    /**
	 * 租户标识用于租户备份和删除
	 */
	private String lesseeId;
	/**
	 * 租户信息
	 */
	private HireInfo hireInfo;
	
	/**
	 * 单位类型
	 */
	private UseunitType useunitType;
	/**
	 * 所属省市(地图显示用)
	 */
	private String pcity;
	/**
	 * 是否启用
	 */
	private Boolean isAllow = true;
	/**
	 * 级别
	 */
	private Integer level;
	/**
	 * 排序号
	 */
	private Integer orderNo;
	/**
	 * 二级部门编码
	 */
	private Integer secondLevelDeptCode;

	
	public Useunit()  {
		
	}
	
	public Useunit(String id) {
		this.id = id;
	}
	
	@Id
    @GenericGenerator(name="idGenerator", strategy="assigned")
    @GeneratedValue(generator="idGenerator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    /**
     * @return 返回 name。
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 要设置的 name。
     */
    public void setName(String name) {
        this.name = name;
    }

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Boolean getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(Boolean isAllow) {
		this.isAllow = isAllow;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public java.lang.String getManageType() {
		return manageType;
	}

	public void setManageType(java.lang.String manageType) {
		this.manageType = manageType;
	}

	public String getLesseeId() {
		return lesseeId;
	}

	public void setLesseeId(String lesseeId) {
		this.lesseeId = lesseeId;
	}
	
	@ManyToOne
    @JoinColumn(name="hire_info_id",nullable = true)
	public HireInfo getHireInfo() {
		return hireInfo;
	}

	public void setHireInfo(HireInfo hireInfo) {
		this.hireInfo = hireInfo;
	}
	
	@ManyToOne
    @JoinColumn(name="useunit_type_id",nullable = true)
	public UseunitType getUseunitType() {
		return useunitType;
	}

	public void setUseunitType(UseunitType useunitType) {
		this.useunitType = useunitType;
	}
	
	public String getPcity() {
		return pcity;
	}

	public void setPcity(String pcity) {
		this.pcity = pcity;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getSecondLevelDeptCode() {
		return secondLevelDeptCode;
	}

	public void setSecondLevelDeptCode(Integer secondLevelDeptCode) {
		this.secondLevelDeptCode = secondLevelDeptCode;
	}

	public Useunit poToVo() {
		Useunit vo = new Useunit();
		vo.setHireInfo(hireInfo!=null?hireInfo.poToVo():null);
		vo.setId(id);
		vo.setIsAllow(isAllow);
		vo.setLesseeId(lesseeId);
		vo.setManageType(manageType);
		vo.setName(name);
		vo.setParentId(parentId);
		vo.setRemark(remark);
		vo.setShortName(shortName);
		vo.setUseunitType(useunitType);
		vo.setPcity(pcity);
		vo.setLevel(level);
		vo.setOrderNo(orderNo);
		vo.setSecondLevelDeptCode(secondLevelDeptCode);
		return vo;
	}
	
	public static List<Useunit> poToVos(List<Useunit> list){
		if(list==null) {return null;}
		List<Useunit> vos = new ArrayList<Useunit>();
		for(Useunit vo : list) {
			vos.add(vo.poToVo());
		}
		return vos;
	}
	
	/**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("name",this.name);
        jsonMap.put("shortName",this.shortName);
        jsonMap.put("remark",this.remark);
        jsonMap.put("manageType",this.manageType);
        jsonMap.put("parentId",this.parentId);
        jsonMap.put("lesseeId",this.lesseeId);
        jsonMap.put("isAllow",this.isAllow);
        jsonMap.put("useunitType", this.useunitType==null?null:this.useunitType.poToMap());
        jsonMap.put("hireInfo", this.hireInfo==null?null:this.hireInfo.poToMap());
        return jsonMap;
    }
}
