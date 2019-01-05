package com.tysoft.entity.base;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 单位表 单位表

 * 创建日期 2019-1-4 21:59:53
 */
@Entity
@Table(name="bs_unit")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Unit implements Serializable{

    private static final long serialVersionUID = 10663550610L;

    public  Unit(){
    }
    public  Unit(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 单位名
     */
    private java.lang.String unitName;

    /**
     * 单位状态
     */
    private java.lang.Integer unitState;

    /**
     *  公司表
     */
    private Company company;

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
     *@return:java.lang.String 单位名
     */
    public java.lang.String getUnitName(){
      return this.unitName;
    }
    /**
     *@param:java.lang.String 单位名
     */
    public void setUnitName(java.lang.String unitName){ 
      this.unitName=unitName;
    }

    /**
     *@return:java.lang.Integer 单位状态
     */
    public java.lang.Integer getUnitState(){
      return this.unitState;
    }
    /**
     *@param:java.lang.Integer 单位状态
     */
    public void setUnitState(java.lang.Integer unitState){ 
      this.unitState=unitState;
    }

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY )
    @JoinColumn(name="company_id",nullable = true)
    public Company getCompany() {
       return company;
    }
    public void setCompany(Company company) {
       this.company = company;
    }

    /**PoToVo*/
    public Unit poToVo() {
        Unit vo = new Unit();
        vo.setId(this.id);
        vo.setUnitName(this.unitName);
        vo.setUnitState(this.unitState);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"unitName\":\"").append(this.getUnitName()).append("\"");
        sb.append(",");
        sb.append("\"unitState\":\"").append(this.getUnitState()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("unitName",this.unitName);
        jsonMap.put("unitState",this.unitState);
        return jsonMap;
    }
}