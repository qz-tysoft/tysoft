

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
 * 公司表 
 * 创建日期 2019-1-4 21:59:53
 */
@Entity
@Table(name="bs_company")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements Serializable{

    private static final long serialVersionUID = 577504293523L;

    public  Company(){
    }
    public  Company(String id){
      this.id = id;
    }

    /**
     * id
     */
    private java.lang.String id;

    /**
     * 公司名
     */
    private java.lang.String companyName;

    /**
     * 状态
     */
    private java.lang.Integer companyState;

    /**
     * 公司人数
     */
    private java.lang.Integer companyNum;

    /**
     * 使用人数
     */
    private java.lang.Integer useNum;

    /**
     * 部门数
     */
    private java.lang.Integer deptNum;

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
     *@return:java.lang.String 公司名
     */
    public java.lang.String getCompanyName(){
      return this.companyName;
    }
    /**
     *@param:java.lang.String 公司名
     */
    public void setCompanyName(java.lang.String companyName){ 
      this.companyName=companyName;
    }

    /**
     *@return:java.lang.Integer 状态
     */
    public java.lang.Integer getCompanyState(){
      return this.companyState;
    }
    /**
     *@param:java.lang.Integer 状态
     */
    public void setCompanyState(java.lang.Integer companyState){ 
      this.companyState=companyState;
    }

    /**
     *@return:java.lang.Integer 公司人数
     */
    public java.lang.Integer getCompanyNum(){
      return this.companyNum;
    }
    /**
     *@param:java.lang.Integer 公司人数
     */
    public void setCompanyNum(java.lang.Integer companyNum){ 
      this.companyNum=companyNum;
    }

    /**
     *@return:java.lang.Integer 使用人数
     */
    public java.lang.Integer getUseNum(){
      return this.useNum;
    }
    /**
     *@param:java.lang.Integer 使用人数
     */
    public void setUseNum(java.lang.Integer useNum){ 
      this.useNum=useNum;
    }

    /**
     *@return:java.lang.Integer 部门数
     */
    public java.lang.Integer getDeptNum(){
      return this.deptNum;
    }
    /**
     *@param:java.lang.Integer 部门数
     */
    public void setDeptNum(java.lang.Integer deptNum){ 
      this.deptNum=deptNum;
    }


    /**PoToVo*/
    public Company poToVo() {
        Company vo = new Company();
        vo.setId(this.id);
        vo.setCompanyName(this.companyName);
        vo.setCompanyState(this.companyState);
        vo.setCompanyNum(this.companyNum);
        vo.setUseNum(this.useNum);
        vo.setDeptNum(this.deptNum);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"companyName\":\"").append(this.getCompanyName()).append("\"");
        sb.append(",");
        sb.append("\"companyState\":\"").append(this.getCompanyState()).append("\"");
        sb.append(",");
        sb.append("\"companyNum\":\"").append(this.getCompanyNum()).append("\"");
        sb.append(",");
        sb.append("\"useNum\":\"").append(this.getUseNum()).append("\"");
        sb.append(",");
        sb.append("\"deptNum\":\"").append(this.getDeptNum()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("companyName",this.companyName);
        jsonMap.put("companyState",this.companyState);
        jsonMap.put("companyNum",this.companyNum);
        jsonMap.put("useNum",this.useNum);
        jsonMap.put("deptNum",this.deptNum);
        return jsonMap;
    }
}