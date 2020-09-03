

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
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import com.tysoft.entity.companyreport.CustomerCompany;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 客户产品表 
 * 创建日期 2020-9-3 14:48:04
 */
@Entity
@Table(name="cr_customer_product")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomerProduct implements Serializable{

    private static final long serialVersionUID = 167806838301L;

    public  CustomerProduct(){
    }
    public  CustomerProduct(String id){
      this.id = id;
    }

    /**
     * id
     */
    private String id;

    /**
     * 产品名
     */
    private String productName;

    /**
     * 规格
     */
    private String specs;

    /**
     * 租户id
     */
    private String lesseeId;

    /**
     *  客户公司设置表
     */
    private CustomerCompany customerCompany;


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
     *@return:java.lang.String 产品名
     */
    @Column(length=1024)
    public String getProductName(){
      return this.productName;
    }
    /**
     *@param:java.lang.String 产品名
     */
    public void setProductName(String productName){
      this.productName=productName;
    }

    /**
     *@return:java.lang.String 规格
     */
    @Column(length=1024)
    public String getSpecs(){
      return this.specs;
    }
    /**
     *@param:java.lang.String 规格
     */
    public void setSpecs(String specs){
      this.specs=specs;
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

    @ManyToOne( cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY )
    @JoinColumn(name="customer_company_id",nullable = true,foreignKey=@ForeignKey(name="fk_rs_company_product"))
    public CustomerCompany getCustomerCompany() {
       return customerCompany;
    }
    public void setCustomerCompany(CustomerCompany customerCompany) {
       this.customerCompany = customerCompany;
    }

    /**PoToVo*/
    public CustomerProduct poToVo() {
        CustomerProduct vo = new CustomerProduct();
        vo.setId(this.id);
        vo.setProductName(this.productName);
        vo.setSpecs(this.specs);
        vo.setLesseeId(this.lesseeId);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"productName\":\"").append(this.getProductName()).append("\"");
        sb.append(",");
        sb.append("\"specs\":\"").append(this.getSpecs()).append("\"");
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
        jsonMap.put("productName",this.productName);
        jsonMap.put("specs",this.specs);
        jsonMap.put("lesseeId",this.lesseeId);
        jsonMap.put("customerCompany", this.customerCompany==null?null:this.customerCompany.poToMap());
        return jsonMap;
    }
}