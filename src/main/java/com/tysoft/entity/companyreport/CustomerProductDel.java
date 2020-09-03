

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
import com.tysoft.entity.companyreport.CustomerProduct;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 客户产品详情表 
 * 创建日期 2020-9-3 14:48:02
 */
@Entity
@Table(name="cr_customer_product_del")
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomerProductDel implements Serializable{

    private static final long serialVersionUID = 376355989795L;

    public  CustomerProductDel(){
    }
    public  CustomerProductDel(String id){
      this.id = id;
    }

    /**
     * id
     */
    private String id;

    /**
     * 数量
     */
    private String productNum;

    /**
     * 单价
     */
    private String productPrice;

    /**
     * 时间
     */
    private java.util.Date productTime;

    /**
     * 附件id
     */
    private String annexIds;

    /**
     * 租户id
     */
    private String lesseeId;

    /**
     *  客户产品表
     */
    private CustomerProduct customerProduct;


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
     *@return:java.lang.String 数量
     */
    @Column(length=1024)
    public String getProductNum(){
      return this.productNum;
    }
    /**
     *@param:java.lang.String 数量
     */
    public void setProductNum(String productNum){
      this.productNum=productNum;
    }

    /**
     *@return:java.lang.String 单价
     */
    @Column(length=1024)
    public String getProductPrice(){
      return this.productPrice;
    }
    /**
     *@param:java.lang.String 单价
     */
    public void setProductPrice(String productPrice){
      this.productPrice=productPrice;
    }

    /**
     *@return:java.util.Date 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public java.util.Date getProductTime(){
      return this.productTime;
    }
    /**
     *@param:java.util.Date 时间
     */
    public void setProductTime(java.util.Date productTime){ 
      this.productTime=productTime;
    }

    /**
     *@return:java.lang.String 附件id
     */
    @Column(length=1024)
    public String getAnnexIds(){
      return this.annexIds;
    }
    /**
     *@param:java.lang.String 附件id
     */
    public void setAnnexIds(String annexIds){
      this.annexIds=annexIds;
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
    @JoinColumn(name="customer_product_id",nullable = true,foreignKey=@ForeignKey(name="fk_rs_product_del"))
    public CustomerProduct getCustomerProduct() {
       return customerProduct;
    }
    public void setCustomerProduct(CustomerProduct customerProduct) {
       this.customerProduct = customerProduct;
    }

    /**PoToVo*/
    public CustomerProductDel poToVo() {
        CustomerProductDel vo = new CustomerProductDel();
        vo.setId(this.id);
        vo.setProductNum(this.productNum);
        vo.setProductPrice(this.productPrice);
        vo.setProductTime(this.productTime);
        vo.setAnnexIds(this.annexIds);
        vo.setLesseeId(this.lesseeId);
       return vo;
    }

    /**PoToJson*/
    public String poToJson() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(this.getId()).append("\"");
        sb.append(",");
        sb.append("\"productNum\":\"").append(this.getProductNum()).append("\"");
        sb.append(",");
        sb.append("\"productPrice\":\"").append(this.getProductPrice()).append("\"");
        sb.append(",");
        sb.append("\"productTime\":\"").append(this.getProductTime() == null ? null : sdf.format(this.getProductTime())).append("\"");
        sb.append(",");
        sb.append("\"annexIds\":\"").append(this.getAnnexIds()).append("\"");
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
        jsonMap.put("productNum",this.productNum);
        jsonMap.put("productPrice",this.productPrice);
        jsonMap.put("productTime",this.productTime == null ? null : sdf.format(this.productTime));
        jsonMap.put("annexIds",this.annexIds);
        jsonMap.put("lesseeId",this.lesseeId);
        jsonMap.put("customerProduct", this.customerProduct==null?null:this.customerProduct.poToMap());
        return jsonMap;
    }
}