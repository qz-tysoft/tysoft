/**
* Description: SEGS SEGS
* Copyright: Copyright (c) 2018
* Company: 厦门路桥信息股份有限公司
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
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import com.tysoft.entity.security.UseunitType;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 单位信息 
 * 创建日期 2018-4-12 15:35:37
 */
@Entity
@Table(name="SS_company_info")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompanyInfo implements Serializable{

    private static final long serialVersionUID = 376355989795165L;

    public  CompanyInfo(){
    }
    public  CompanyInfo(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 企业名称
     */
    private java.lang.String name;

    /**
     * 统一社会信用代码
     */
    private java.lang.String socialCreditCode;

    /**
     *  单位类型
     */
    private UseunitType useunitType;

    private Boolean isUpdate;

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
     *@return:java.lang.String 企业名称
     */
    public java.lang.String getName(){
      return this.name;
    }
    /**
     *@param:java.lang.String 企业名称
     */
    public void setName(java.lang.String name){ 
      this.name=name;
    }

    /**
     *@return:java.lang.String 统一社会信用代码
     */
    public java.lang.String getSocialCreditCode(){
      return this.socialCreditCode;
    }
    /**
     *@param:java.lang.String 统一社会信用代码
     */
    public void setSocialCreditCode(java.lang.String socialCreditCode){ 
      this.socialCreditCode=socialCreditCode;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean update) {
        isUpdate = update;
    }

    @ManyToOne
    @JoinColumn(name="useunit_type_id",nullable = true)
    public UseunitType getUseunitType() {
       return useunitType;
    }
    public void setUseunitType(UseunitType useunitType) {
       this.useunitType = useunitType;
    }
    public CompanyInfo poToVo() {
    	CompanyInfo vo = new CompanyInfo();
       vo.setId(this.id);
       vo.setName(this.name);
       vo.setSocialCreditCode(this.socialCreditCode);
       return vo;
    }
}
