/**
* <p>Description: SEGS_CLOUD SEGS_CLOUD</p>
*
* <p>Copyright: Copyright (c) 2018</p>
*
* <p>Company: 厦门路桥信息股份有限公司</p>
*
* @author :lsk
* @version 1.0
*/

package com.tysoft.entity.project;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 项目区域 
 * 创建日期 2018-4-24 15:22:18
 */
@Entity
@Table(name="PM_project_area")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectArea implements Serializable{

    private static final long serialVersionUID = 376355989795L;

    public  ProjectArea(){
    }
    public  ProjectArea(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 区域/分类名称
     */
    private java.lang.String areaName;

    /**
     * 说明
     */
    private java.lang.String remark;
    /**
     * 所属企业
     */
    private String lesseeId;

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    /**
     *@return:java.lang.String id
     */
    public java.lang.String getId(){
      return this.id;
    }
    /**
     *@param:java.lang.String id
     */
    public void setId(java.lang.String id){ 
      this.id=id;
    }

    /**
     *@return:java.lang.String 区域/分类名称
     */
    public java.lang.String getAreaName(){
      return this.areaName;
    }
    /**
     *@param:java.lang.String 区域/分类名称
     */
    public void setAreaName(java.lang.String areaName){ 
      this.areaName=areaName;
    }

    /**
     *@return:java.lang.String 说明
     */
    public java.lang.String getRemark(){
      return this.remark;
    }
    /**
     *@param:java.lang.String 说明
     */
    public void setRemark(java.lang.String remark){ 
      this.remark=remark;
    }

    public String getLesseeId() {
		return lesseeId;
	}
	public void setLesseeId(String lesseeId) {
		this.lesseeId = lesseeId;
	}
	public ProjectArea poToVo() {
    ProjectArea vo = new ProjectArea();
       vo.setId(this.id);
       vo.setAreaName(this.areaName);
       vo.setRemark(this.remark);
       vo.setLesseeId(this.lesseeId);
       return vo;
    }
}