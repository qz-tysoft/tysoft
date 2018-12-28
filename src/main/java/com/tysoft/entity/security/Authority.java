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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 权限 
 * 创建日期 2018-3-9 17:23:08
 */
@Entity
@Table(name="SS_authority")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Authority implements Serializable{

    private static final long serialVersionUID = 18777524318338L;
    
    public static final String TYPE_TOP = "1";//主功能菜单
    public static final String TYPE_LEFT = "2";//左侧功能菜单
    public static final String TYPE_DESKTOP = "3";//桌面块级功能菜单
    public static final String TYPE_DESKTOP_LEFT = "4";//桌面块级下左侧菜单
    public static final String TYPE_AUTHORITY = "5";//非菜单权限
    

    public  Authority(){
    }
    public  Authority(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 权限名称
     */
    private java.lang.String name;
    
    /**
     * 功能菜单类型
     */
    private java.lang.String authorityType;

    /**
     * 图标名称或图标url地址
     */
    private java.lang.String icon;

    /**
     * url地址
     */
    private java.lang.String url;

    /**
     * 排序
     */
    private java.lang.Integer sort;

    /**
     *  父级权限
     */
    private String parentId;
    /**
     * 默认显示的角色
     */
    private String defaultRoleTypes;
    
    /**
     * 节点集合
     */
    private String treeCode;

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    public java.lang.String getId(){
      return this.id;
    }
    /**
     *@param:java.lang.Integer id
     */
    public void setId(java.lang.String id){ 
      this.id=id;
    }

    /**
     *@return:java.lang.String 权限名称
     */
    public java.lang.String getName(){
      return this.name;
    }
    /**
     *@param:java.lang.String 权限名称
     */
    public void setName(java.lang.String name){ 
      this.name=name;
    }

    /**
     *@return:java.lang.String 图标名称
     */
    public java.lang.String getIcon(){
      return this.icon;
    }
    /**
     *@param:java.lang.String 图标名称
     */
    public void setIcon(java.lang.String icon){ 
      this.icon=icon;
    }

    /**
     *@return:java.lang.String url地址
     */
    public java.lang.String getUrl(){
      return this.url;
    }
    /**
     *@param:java.lang.String url地址
     */
    public void setUrl(java.lang.String url){ 
      this.url=url;
    }

    public java.lang.String getAuthorityType() {
		return authorityType;
	}
	public void setAuthorityType(java.lang.String authorityType) {
		this.authorityType = authorityType;
	}
	/**
     *@return:java.lang.Integer 排序
     */
    public java.lang.Integer getSort(){
      return this.sort;
    }
    /**
     *@param:java.lang.Integer 排序
     */
    public void setSort(java.lang.Integer sort){ 
      this.sort=sort;
    }
	/**
	 * 父级ID
	 * @return
	 */
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public String getDefaultRoleTypes() {
		return defaultRoleTypes;
	}
	public void setDefaultRoleTypes(String defaultRoleTypes) {
		this.defaultRoleTypes = defaultRoleTypes;
	}
	
	public String getTreeCode() {
		return treeCode;
	}
	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}
	public Authority poToVo() {
		Authority vo = new Authority();
       vo.setId(this.id);
       vo.setName(this.name);
       vo.setIcon(this.icon);
       vo.setUrl(this.url);
       vo.setSort(this.sort);
       vo.setParentId(this.parentId);
       vo.setAuthorityType(this.authorityType);
       vo.setDefaultRoleTypes(this.defaultRoleTypes);
       vo.setTreeCode(this.treeCode);
       return vo;
    }
}
