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

package com.tysoft.entity.security;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 企业详细信息 
 * 创建日期 2018-5-2 11:29:52
 */
@Entity
@Table(name="SS_hire_info")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HireInfo implements Serializable{

    private static final long serialVersionUID = 376355989795L;

    public  HireInfo(){
    }
    public  HireInfo(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 企业最大人数
     */
    private java.lang.Integer maxUserNum;

    /**
     * 在用人数
     */
    private java.lang.Integer inuseUserNum;

    /**
     * 部门数
     */
    private java.lang.Integer departmentNum;

    /**
     * 图标
     */
    private java.lang.String logo;

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
     *@return:java.lang.Integer 企业最大人数
     */
    public java.lang.Integer getMaxUserNum(){
      return this.maxUserNum;
    }
    /**
     *@param:java.lang.Integer 企业最大人数
     */
    public void setMaxUserNum(java.lang.Integer maxUserNum){ 
      this.maxUserNum=maxUserNum;
    }

    /**
     *@return:java.lang.Integer 在用人数
     */
    public java.lang.Integer getInuseUserNum(){
      return this.inuseUserNum;
    }
    /**
     *@param:java.lang.Integer 在用人数
     */
    public void setInuseUserNum(java.lang.Integer inuseUserNum){ 
      this.inuseUserNum=inuseUserNum;
    }

    /**
     *@return:java.lang.Integer 部门数
     */
    public java.lang.Integer getDepartmentNum(){
      return this.departmentNum;
    }
    /**
     *@param:java.lang.Integer 部门数
     */
    public void setDepartmentNum(java.lang.Integer departmentNum){ 
      this.departmentNum=departmentNum;
    }

    /**
     *@return:java.sql.Blob 图标
     */
    @Lob 
    @Basic(fetch = FetchType.LAZY) 
    @Column(name = "logo", columnDefinition = "LONGBLOB",nullable=true)
    public java.lang.String getLogo(){
      return this.logo;
    }
    /**
     *@param:java.sql.Blob 图标
     */
    public void setLogo(java.lang.String logo){ 
      this.logo=logo;
    }

    public HireInfo poToVo() {
    HireInfo vo = new HireInfo();
       vo.setId(this.id);
       vo.setMaxUserNum(this.maxUserNum);
       vo.setInuseUserNum(this.inuseUserNum);
       vo.setDepartmentNum(this.departmentNum);
       vo.setLogo(this.logo);
       return vo;
    }
    
    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("maxUserNum",this.maxUserNum);
        jsonMap.put("inuseUserNum",this.inuseUserNum);
        jsonMap.put("departmentNum",this.departmentNum);
        jsonMap.put("logo",this.logo);
        return jsonMap;
    }
}