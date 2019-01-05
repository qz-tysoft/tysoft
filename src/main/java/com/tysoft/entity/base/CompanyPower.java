package com.tysoft.entity.base;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 公司权限表 
 * 创建日期 2019-1-4 21:59:52
 */
@Entity
@Table(name="bs_company_power")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompanyPower implements Serializable{

    private static final long serialVersionUID = 376355989795L;

    public  CompanyPower(){
    }
    public  CompanyPower(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 权限id
     */
    private java.lang.String powerId;

    /**
     * 公司id
     */
    private java.lang.String companyId;

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
     *@return:java.lang.String 权限id
     */
    public java.lang.String getPowerId(){
      return this.powerId;
    }
    /**
     *@param:java.lang.String 权限id
     */
    public void setPowerId(java.lang.String powerId){ 
      this.powerId=powerId;
    }

    /**
     *@return:java.lang.String 公司id
     */
    public java.lang.String getCompanyId(){
      return this.companyId;
    }
    /**
     *@param:java.lang.String 公司id
     */
    public void setCompanyId(java.lang.String companyId){ 
      this.companyId=companyId;
    }


    /**PoToVo*/
    public CompanyPower poToVo() {
        CompanyPower vo = new CompanyPower();
        vo.setId(this.id);
        vo.setPowerId(this.powerId);
        vo.setCompanyId(this.companyId);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"powerId\":\"").append(this.getPowerId()).append("\"");
        sb.append(",");
        sb.append("\"companyId\":\"").append(this.getCompanyId()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("powerId",this.powerId);
        jsonMap.put("companyId",this.companyId);
        return jsonMap;
    }
}