

package com.tysoft.entity.companyreport;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Column;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 客户公司设置表 
 * 创建日期 2020-9-3 14:48:05
 */
@Entity
@Table(name="cr_customer_company")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomerCompany implements Serializable{

    private static final long serialVersionUID = 233931232148L;

    public  CustomerCompany(){
    }
    public  CustomerCompany(String id){
      this.id = id;
    }

    /**
     * id
     */
    private String id;

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 租户id
     */
    private String lesseeId;


    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    /**
     *@return:java.lang.String id
     */
    @Column(length=1024)
    public String getId(){
      return this.id;
    }
    /**
     *@param:java.lang.String id
     */
    public void setId(String id){
      this.id=id;
    }

    /**
     *@return:java.lang.String 公司名
     */
    @Column(length=1024)
    public String getCompanyName(){
      return this.companyName;
    }
    /**
     *@param:java.lang.String 公司名
     */
    public void setCompanyName(String companyName){
      this.companyName=companyName;
    }

    /**
     *@return:java.lang.String 租户id
     */
    @Column(length=1024)
    public String getLesseeId(){
      return this.lesseeId;
    }
    /**
     *@param:java.lang.String 租户id
     */
    public void setLesseeId(String lesseeId){
      this.lesseeId=lesseeId;
    }


    /**PoToVo*/
    public CustomerCompany poToVo() {
        CustomerCompany vo = new CustomerCompany();
        vo.setId(this.id);
        vo.setCompanyName(this.companyName);
        vo.setLesseeId(this.lesseeId);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"companyName\":\"").append(this.getCompanyName()).append("\"");
        sb.append(",");
        sb.append("\"lesseeId\":\"").append(this.getLesseeId()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    /**PoToMap*/
    public Map<String, Object> poToMap() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",this.id);
        jsonMap.put("companyName",this.companyName);
        jsonMap.put("lesseeId",this.lesseeId);
        return jsonMap;
    }
}