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
 * 权限表 
 * 创建日期 2019-1-4 22:16:29
 */
@Entity
@Table(name="bs_power")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Power implements Serializable{

    private static final long serialVersionUID = 167806838301L;

    public  Power(){
    }
    public  Power(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 权限名称
     */
    private java.lang.String powerName;

    /**
     * 地址
     */
    private java.lang.String url;

    /**
     * 父级id
     */
    private java.lang.Integer fatherId;

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
     *@return:java.lang.String 权限名称
     */
    public java.lang.String getPowerName(){
      return this.powerName;
    }
    /**
     *@param:java.lang.String 权限名称
     */
    public void setPowerName(java.lang.String powerName){ 
      this.powerName=powerName;
    }

    /**
     *@return:java.lang.String 地址
     */
    public java.lang.String getUrl(){
      return this.url;
    }
    /**
     *@param:java.lang.String 地址
     */
    public void setUrl(java.lang.String url){ 
      this.url=url;
    }

    /**
     *@return:java.lang.Integer 父级id
     */
    public java.lang.Integer getFatherId(){
      return this.fatherId;
    }
    /**
     *@param:java.lang.Integer 父级id
     */
    public void setFatherId(java.lang.Integer fatherId){ 
      this.fatherId=fatherId;
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
    public Power poToVo() {
        Power vo = new Power();
        vo.setId(this.id);
        vo.setPowerName(this.powerName);
        vo.setUrl(this.url);
        vo.setFatherId(this.fatherId);
        vo.setCompanyId(this.companyId);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"powerName\":\"").append(this.getPowerName()).append("\"");
        sb.append(",");
        sb.append("\"url\":\"").append(this.getUrl()).append("\"");
        sb.append(",");
        sb.append("\"fatherId\":\"").append(this.getFatherId()).append("\"");
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
        jsonMap.put("powerName",this.powerName);
        jsonMap.put("url",this.url);
        jsonMap.put("fatherId",this.fatherId);
        jsonMap.put("companyId",this.companyId);
        return jsonMap;
    }
}