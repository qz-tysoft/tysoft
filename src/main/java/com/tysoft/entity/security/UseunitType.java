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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 单位类型 
 * 创建日期 2018-3-9 17:23:07
 */
@Entity
@Table(name="SS_useunit_type")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UseunitType implements Serializable{

    private static final long serialVersionUID = 376355989795165L;

    public  UseunitType(){
    }
    public  UseunitType(Integer id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.Integer id;

    /**
     * 编号
     */
    private java.lang.String no;

    /**
     * 类型名称
     */
    private java.lang.String typeName;

    /**
     * 是否删除
     */
    private java.lang.Boolean isDeleted = false;
    
    private java.lang.Boolean isUpdate = false;

    @Id
    @GeneratedValue
    public java.lang.Integer getId(){
      return this.id;
    }
    /**
     *@param:java.lang.Integer id
     */
    public void setId(java.lang.Integer id){ 
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
     *@return:java.lang.String 类型名称
     */
    public java.lang.String getTypeName(){
      return this.typeName;
    }
    /**
     *@param:java.lang.String 类型名称
     */
    public void setTypeName(java.lang.String typeName){ 
      this.typeName=typeName;
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
     * @param:java.lang.Boolean 是否更新
     */
	public void setIsUpdate(java.lang.Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	
	public UseunitType poToVo() {
    UseunitType vo = new UseunitType();
       vo.setId(this.id);
       vo.setNo(this.no);
       vo.setTypeName(this.typeName);
       vo.setIsDeleted(this.isDeleted);
       vo.setIsUpdate(this.isUpdate);
       return vo;
    }
	
	/**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("no",this.no);
        jsonMap.put("typeName",this.typeName);
        jsonMap.put("isDeleted",this.isDeleted);
        return jsonMap;
    }
}
