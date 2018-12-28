/**
* Description: SEGS SEGS
*
* Copyright: Copyright (c) 2018
*
* Company: 厦门路桥信息股份有限公司
*
* @author :Administrator
* @version 1.0
*/

package com.tysoft.entity.security;

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
 * 管理人员类型 
 * 创建日期 2018-3-29 11:34:29
 */
@Entity
@Table(name="SS_admin_type")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminType implements Serializable{

    private static final long serialVersionUID = 376355989795165L;

    public  AdminType(){
    }
    public  AdminType(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 编号
     */
    private java.lang.String no;

    /**
     * 分类
     */
    private java.lang.String groupName;

    /**
     * 类型名称
     */
    private java.lang.String name;

    /**
     * 是否删除
     */
    private java.lang.Boolean isDeleted = false;
    
    /**
     * 是否更新
     */
    private java.lang.Boolean isUpdate = false;
    
    /**
     *@return:java.lang.String id
     */
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
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
     *@return:java.lang.String 编号
     */
    public java.lang.String getNo(){
      return this.no;
    }
    /**
     *@param:java.lang.String 编号
     */
    public void setNo(java.lang.String no){ 
      this.no=no;
    }

    /**
     *@return:java.lang.String 分类
     */
    public java.lang.String getGroupName(){
      return this.groupName;
    }
    /**
     *@param:java.lang.String 分类
     */
    public void setGroupName(java.lang.String groupName){ 
      this.groupName=groupName;
    }

    /**
     *@return:java.lang.String 类型名称
     */
    public java.lang.String getName(){
      return this.name;
    }
    /**
     *@param:java.lang.String 类型名称
     */
    public void setName(java.lang.String name){ 
      this.name=name;
    }

    /**
     *@return:java.lang.Boolean 是否删除
     */
    public java.lang.Boolean getIsDeleted(){
      return this.isDeleted;
    }
    /**
     *@param:java.lang.Boolean 是否删除
     */
    public void setIsDeleted(java.lang.Boolean isDeleted){ 
      this.isDeleted=isDeleted;
    }
    
    /**
     * @return:java.lang.Boolean 是否更新
     */
    public java.lang.Boolean getIsUpdate() {
		return isUpdate;
	}
    /**
     * @param java.lang.Boolean 是否更新
     */
	public void setIsUpdate(java.lang.Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	public AdminType poToVo() {
    AdminType vo = new AdminType();
       vo.setId(this.id);
       vo.setNo(this.no);
       vo.setGroupName(this.groupName);
       vo.setName(this.name);
       vo.setIsDeleted(this.isDeleted);
       vo.setIsUpdate(this.isUpdate);
       return vo;
    }
}
